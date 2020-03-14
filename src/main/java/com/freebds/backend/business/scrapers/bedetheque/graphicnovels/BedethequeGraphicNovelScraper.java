package com.freebds.backend.business.scrapers.bedetheque.graphicnovels;

import com.freebds.backend.business.scrapers.GenericScraper;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedAuthorRole;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BedethequeGraphicNovelScraper extends GenericScraper {

    public BedethequeGraphicNovelScraper() {
    }

    /**
     * Scrap all graphic novels from a serie
     * @param serieUrl
     * @return
     * @throws IOException
     */
    public List<ScrapedGraphicNovel> retrieveAlbums(String serieUrl) throws IOException {
        // Load all authors starting with the letter
        Document doc = this.load(serieUrl);

        List<ScrapedGraphicNovel> scrapedGraphicNovels = new ArrayList<ScrapedGraphicNovel>();

        Elements eAlbums = doc.select("ul.liste-albums li[itemtype='https://schema.org/Book']");

        for(Element li : eAlbums) {
            scrapedGraphicNovels.add(scrap(li));
        }

        return scrapedGraphicNovels;
    }

    /**
     * Scrap a graphic novel from the complete serie list
     * @param nodeAlbum
     * @return
     */
    public ScrapedGraphicNovel scrap(Element nodeAlbum) {
        ScrapedGraphicNovel scrapedGraphicNovel = new ScrapedGraphicNovel();

        // (re)Publication number
        scrapedGraphicNovel.setNumEdition(nodeAlbum.select("h3 span.numa").first().ownText().trim());

        // Tome / Publication number / Album url
        String gcTitle = null;
        String[] blocTome = nodeAlbum.selectFirst("h3 span[itemprop=name]").html().split("(<span class=\"numa\">)");
        if(blocTome.length >= 1) {
            scrapedGraphicNovel.setTome(blocTome[0].trim());
            if(scrapedGraphicNovel.getTome() == null){
                scrapedGraphicNovel.setTome("");
            }
        } else {
            scrapedGraphicNovel.setTome("");
        }

        // Isolation du titre
        String[] blocTitle = nodeAlbum.selectFirst("h3 span[itemprop=name]").html().split("(<\\/span>)");
        if(blocTitle.length > 1){
            gcTitle = blocTitle[1].trim();
        } else {
            gcTitle = blocTitle[0].trim();
        }

        // Delete first .
        if(gcTitle.startsWith(".")){
            gcTitle = gcTitle.substring(1);
        }

//        //String[] blocTitle = nodeAlbum.selectFirst("h3 span[itemprop=name]").ownText().split("\\.");
//        String[] blocTitle = nodeAlbum.selectFirst("h3 span[itemprop=name]").html().split("(<span class=\"numa\"><\\/span>)");
//        // One bloc only -> serie one shot
//        if(blocTitle.length == 1) {
//            scrapedGraphicNovel.setTome("");
//            scrapedGraphicNovel.setTitle(blocTitle[0].trim());
//            gcTitle = blocTitle[0].trim().substring(1);
//        } else {
//            // More -> Tome
//            blocTitle = nodeAlbum.selectFirst("h3 span[itemprop=name]").html().split("(<span class=\"numa\">)");
//            scrapedGraphicNovel.setTome(blocTitle[0].trim());
//            // + title
//            blocTitle = nodeAlbum.selectFirst("h3 span[itemprop=name]").html().split("(<\\/span>)");
//            gcTitle = blocTitle[0].trim().substring(1);
//        }

        // Title
        scrapedGraphicNovel.setTitle(nodeAlbum.select("div.album-main a.titre").first().attr("title").trim());
        if(scrapedGraphicNovel.getTitle() == null || scrapedGraphicNovel.getTitle().equals("")){
            scrapedGraphicNovel.setTitle(gcTitle);
        }
        // Album url
        scrapedGraphicNovel.setAlbumUrl(nodeAlbum.select("div.album-main a.titre").first().attr("href").trim());

        // Couverture (recto)
        try {
            scrapedGraphicNovel.setCoverPictureUrl(nodeAlbum.selectFirst("div.sous-couv a.browse-couvertures").attr("href").trim());
            scrapedGraphicNovel.setCoverThumbnailUrl(nodeAlbum.selectFirst("div.sous-couv a.browse-couvertures img").attr("src").trim());
        } catch (Exception e) {
            scrapedGraphicNovel.setCoverPictureUrl(nodeAlbum.selectFirst("div.couv img").attr("src").trim());
            scrapedGraphicNovel.setCoverThumbnailUrl(nodeAlbum.selectFirst("div.couv img").attr("src").trim());
            //e.printStackTrace();
            //System.out.println("No front cover found");
        }
        // Planche
        try {
            scrapedGraphicNovel.setPagePictureUrl(nodeAlbum.selectFirst("div.sous-couv a.browse-planches").attr("href").trim());
            scrapedGraphicNovel.setPageThumbnailUrl(nodeAlbum.selectFirst("div.sous-couv a.browse-planches img").attr("src").trim());
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("No extract page found");
        }
        // Verso
        try {
            scrapedGraphicNovel.setBackCoverPictureUrl(nodeAlbum.selectFirst("div.sous-couv a.browse-versos").attr("href").trim());
            scrapedGraphicNovel.setBackCoverThumbnailUrl(nodeAlbum.selectFirst("div.sous-couv a.browse-versos img").attr("src").trim());
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("No back cover found");
        }

        // Search for graphic novel properties
        List<Element> infos = nodeAlbum.select("ul.infos li");

        String lastLineInfoKey = null;
        for(Element li : infos) {
            Map<String, String>  map = getInfosProperty(li, lastLineInfoKey);
            for(Map.Entry<String, String> entry : map.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                if(key != null && key.endsWith("_href") == false){
                    lastLineInfoKey = key;
                }

                //System.out.println("key=" + key + ", value=" + value);

                // External id
                if(key.equals("Identifiant")){
                    scrapedGraphicNovel.setExternalId(value);
                }

                // Authors roles :
                //  -> Scénario, Dessin, Couleurs, Storyboard, Encrage, Lettrage, Couverture,
                //     Autres, Décors, Traduction, Préface, Adapté de, Design
                if(key.equals("Scénario") || key.equals("Dessin") || key.equals("Couleurs") || key.equals("Storyboard") ||
                key.equals("Encrage") || key.equals("Lettrage") || key.equals("Couverture") || key.equals("Autres") ||
                key.equals("Décors") || key.equals("Traduction") || key.equals("Préface") || key.equals("Adapté de") ||
                key.equals("Design")){
                    ScrapedAuthorRole scrapedAuthorRole = new ScrapedAuthorRole();
                    scrapedAuthorRole.setName(value);
                    scrapedAuthorRole.setRole(key);
                    scrapedAuthorRole.setAuthorUrl(map.get(key + "_href"));
                    // Extract id from url
                    String externalId = null;
                    try {
                        externalId = scrapedAuthorRole.getAuthorUrl().split("-")[1];
                        scrapedAuthorRole.setExternalId(externalId);
                    } catch (Exception e) {
                        externalId = "1";
                        scrapedAuthorRole.setExternalId("1");
                        scrapedAuthorRole.setName("<Indéterminé>");
                        scrapedAuthorRole.setRole("<Indéterminé>");
                        scrapedAuthorRole.setAuthorUrl("https://www.bedetheque.com/auteur-1-BD-Indetermine.html");
                        //e.printStackTrace();
                    }


                    scrapedGraphicNovel.addAuthor(scrapedAuthorRole);
                    //System.out.println(scrapedAuthorRole);
                }

                // Dépôt légal
                if(key.equals("Dépot légal")){
                    if(value.contains("Parution")){
                        value = value.replace('(', ' ');
                        value = value.replace(')', ' ');
                        String[] tbParution = value.split("Parution le");

                        scrapedGraphicNovel.setPublicationDate(tbParution[0].trim());

                        if(tbParution.length > 1){
                            scrapedGraphicNovel.setReleaseDate(tbParution[1].trim());
                        }
                    } else {
                        scrapedGraphicNovel.setPublicationDate(value);
                    }
                }

                // Format
                if(key.equals("Format")){
                    scrapedGraphicNovel.setFormat(value);
                }

                // Publisher
                if(key.equals("Editeur")){
                    scrapedGraphicNovel.setPublisher(value);
                }

                // Collection / Cycle
                if(key.equals("Collection")){
                    scrapedGraphicNovel.setCollection(value);
                }

                // ISBN
                if(key.equals("ISBN")){
                    scrapedGraphicNovel.setIsbn(value);
                }

                // Pages
                if(key.equals("Planches")){
                    try {
                        scrapedGraphicNovel.setTotalPages(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        //e.printStackTrace();
                    }
                }

                // Original edition
                if(key.equals("editionOriginale")){
                    scrapedGraphicNovel.setOriginalPublication(true);
                }

                // Integrale
                if(key.equals("integrale")){
                    scrapedGraphicNovel.setIntegrale(true);
                }

                // Broché
                if(key.equals("broche")) {
                    scrapedGraphicNovel.setBroche(true);
                }
            }
        }

        // Scrap Infos Edition / Reedition
        Map<String, String>  mapEdition = getOtherProperties(nodeAlbum);
        for(Map.Entry<String, String> entry : mapEdition.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //System.out.println("key=" + key + ", value=" + value);

            // Infos Edition
            if (key.equals("infosEdition")) {
                scrapedGraphicNovel.setInfoEdition(value);
            }

            // Reediton url
            if(key.equals("reeditions")){
                scrapedGraphicNovel.setReeditionUrl(value);
            }
        }

        //System.out.println(scrapedGraphicNovel.toString());
        // Date
        scrapedGraphicNovel.setCreationDate(LocalDateTime.now());
        scrapedGraphicNovel.setCreationUser("SCRAPER_BEDETHEQUE_V1");
        scrapedGraphicNovel.setLastUpdateDate(LocalDateTime.now());
        scrapedGraphicNovel.setLastUpdateUser("SCRAPER_BEDETHEQUE_V1");

        return scrapedGraphicNovel;
    }

    /**
     * Scrap main infos
     *   Authors roles :
     *     -> Scénario, Dessin, Couleurs, Storyboard, Encrage, Lettrage, Couverture,
     *        Autres, Décors, Traduction, Préface, Adapté de, Design
     *   Dépôt légal
     *   Date de parution
     *   Editeur
     *   Collection
     *   ISBN
     *   Format
     *   Edition originale
     *   Intégrale
     *   Broché
     *   TODO : Tirage de tête
     * @param li
     * @return
     */
    private Map<String, String> getInfosProperty(Element li, String lastLineInfoKey) {
        String key = null;
        String value = null;
        String href = null;
        Map<String, String> property = new HashMap<String, String>();

        // Get key
        key = li.selectFirst("label").ownText().toString();

        try {
            key = key.substring(0, key.length()-2);
        } catch (Exception e) {
            key = lastLineInfoKey;
        }

        // Get value
        // Link?
        Element nodeLink = li.selectFirst("a");
        if(nodeLink == null) {
            // Span?
            Element nodeSpan = li.selectFirst("span");
            if(nodeSpan != null) {
                value = li.ownText().trim() + nodeSpan.ownText().trim();
            } else {
                value = li.ownText().trim();
            }

        } else {
            // Span?
            Element nodeSpan = nodeLink.selectFirst("span");
            if(nodeSpan != null) {
                value = nodeSpan.ownText().trim();
            } else {
                value = nodeLink.ownText().trim();
            }
            href = nodeLink.attr("href");
        }

        if(key != null && key.compareTo("") > 0) {
            property.put(key, value);
            if(href != null){
                property.put(key +"_href", href);
            }
        }


        // Autres infos
        if(key.compareTo("Autres infos") == 0) {
            // Edition originale
            if(li.selectFirst(".icon-star") != null)
                property.put("editionOriginale", "true");

            // Intégrale
            if(li.selectFirst(".icon-pause") != null)
                property.put("integrale", "true");

            // Broché (couverture souple)
            if(li.selectFirst(".icon-tag") != null)
                property.put("broche", "true");

            // Tirage de tête
            // TODO

        }

        //System.out.println(key + " = " + value + " - href=" + href);
        //System.out.println(key + " = " + value + " - RAW=" + li.toString());
        return property;
    }

    /**
     * Scrap Infos Edition / Reedition
     * @param li
     * @return
     */
    private Map<String, String> getOtherProperties(Element li) {
        Map<String, String> otherProperties = new HashMap<String, String>();

        // Infos édition
        String infosEdition = null;
        try {
            infosEdition = li.selectFirst("div.autres p").ownText().trim();
            otherProperties.put("infosEdition", infosEdition);
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("No infos edition found");
        }

        // Rééditions
        if(infosEdition != null) {
            try {
                String href = li.selectFirst("div.autres a").attr("href");
                if(href != null) {
                    otherProperties.put("reeditions", href);
                }
            } catch (Exception e) {
                //e.printStackTrace();
                //System.out.println("No reeditions found");
            }
        }

        return otherProperties;
    }


}
