package com.freebds.freebdsbackend.repository;

import com.freebds.freebdsbackend.model.Author;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorControllerTest {

    static Long idLastCreatedAuthor;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    public void createAuthorOk() {
        // Given Author
        Author author = new Author();
        author.setNickname("Greg");
        author.setLastname("Regnier");
        author.setFirstname("Michel");
        author.setNationality("BELGIQUE");
        author.setExternalId("77");
        author.setBirthdate(LocalDate.of(1931, 5, 5));
        author.setDeceaseDate(LocalDate.of(1999,10,29));
        author.setSiteUrl("http://michel.greg.free.fr");
        author.setBiography("Greg, de son vrai nom Michel Regnier, naît le 5 mai 1931 à Ixelles, une commune de Bruxelles. Il est cependant de nationalité française. Dès l'école communale, dans un quartier « à risques », où ses petits camarades ne pensent qu'à castagner leur prochain, il sauve sa peau en racontant des histoires : chaque matin, on l'attend sur le chemin de l'école pour savoir la suite, et il a tout intérêt à se montrer talentueux. Cette vocation, bien qu'un peu forcée au départ, ne le quittera jamais. Par ailleurs, il dessine pour le plaisir des Pieds Nickelés et des Tarzan. À 16 ans, il signe les mésaventures humoristiques de Nestor et Boniface, puis du cow-boy Ted Aclak dans le quotidien belge Vers l'Avenir, mais ses talents de dessinateur ne sont guère saisissants et une bonne âme lui conseille d'aller consulter Franquin - autant dire Dieu lui-même. À chaque entrevue, Franquin le félicite chaleureusement pour les progrès accomplis, tout en lui suggérant d'améliorer le nez, les oreilles, les pieds, et aussi le monsieur qu'on voit derrière. Récupérant sa page gribouillée du haut en bas, Greg hésite entre sauter de joie et se flinguer tout de suite. Pourtant, il progresse... En 1953, pour le magazine Héroïc-Albums, il crée Le Chat, une sorte de Batman dont il n'est pas spécialement fier, mais qui fait maintenant courir les collectionneurs. En 1954, le Journal Spirou publie ses aventures de Caddy («Le Temple aux Tigres»), puis de Dopy et Badino («La grande Corrida»). En 1955, il fonde le Journal de Paddy dont, sous divers pseudonymes, il est pratiquement l'unique rédacteur-dessinateur. Confronté à de sordides problèmes d'argent, le périodique ne vivra que 5 numéros. Il entre ensuite à l'agence International Press, où Goscinny et Uderzo viennent de se faire virer après avoir osé réclamer une augmentation... En 1957, tandis que pour le supplément «Junior» de La Libre Belgique, il conte entre autres les histoires de Fleurette et assure, après Uderzo, la reprise de Luc Junior créé par Sirius, Franquin lui confie l'invention de gags de Modeste et Pompon. (Pour la petite histoire, Goscinny a créé dans cette série le voisin Dubruit et Greg, le voisin Ducrin - lesquels ne se rencontrent jamais : on sait ainsi à qui appartient chaque gag, ce qui facilite la comptabilité...). En 1958, tout en écrivant pour Franquin quelques albums de Spirou et Fantasio, dont la fameuse série des Zorglub, Greg couvre l'Exposition universelle de Bruxelles et propose ses excédents d'information au Journal Tintin. Malgré sa totale ignorance du sujet, on lui confie une rubrique automobile et il devient ainsi l'un des collaborateurs réguliers de l'hebdomadaire des jeunes de 7 à 77 ans. En 1963, pour le magazine Vaillant (puis Pif-Gadget), il lance Les As et illustre leurs exploits (édités en albums chez Dargaud). C'est au Journal Tintin (dont il sera le rédacteur en chef et le grand rénovateur de 1964 à 1973) qu'il s'impose comme l'auteur le plus éclectique et le plus prolifique de la BD francophone : plus de 250 albums au total! En plus de ses propres publications comme auteur dessinateur, telles Rock Derby et Babiole et Zou, il produit des scénarios pour Tibet (Chick Bill, Les Peur-de-Rien), Cuvelier (Corentin, Flamme d'Argent, Line), Chéret (Domino), Aidans (Les Panthères), Mittéï (Rouly-la-Brise), Derib (Go West), Auclair (Les Naufragés d'Arroyoka), Fährer (Cobalt), Dupa (Chlorophylle), Turk et de Groot (Clifton), Maréchal (Prudence Petitpas) et bien d'autres... Pour Le Lombard, il écrit surtout 16 albums de Luc Orient pour Paape, 11 albums de Bruno Brazil pour Vance, 13 albums de Bernard Prince pour Hermann et 2 pour Dany, 10 albums de Comanche pour Hermann, 11 albums d'Olivier Rameau (sa série préférée) pour Dany... Sans oublier, bien sûr, les 42 albums publiés chez Dargaud consacrés aux états d'âme alambiqués d'Achille Talon qui, lui, est né dans Pilote de la façon suivante: en 1963, alors que Greg vient de reprendre (pour 5 albums) le Zig et Puce d'Alain Saint-Ogan, Goscinny lui téléphone. Il a besoin de gags en une page, susceptibles de sauter à la dernière minute pour laisser la place à une pub. C'est le genre de faveur qu'on ne peut demander qu'à un copain... Greg s'y colle et crée Achille Talon. Quelques mois plus tard, Goscinny le rappelle et lui dit en substance: «Depuis que ton imbécile est dans le journal, tout le courrier des lecteurs parle comme lui, maintenant tu as deux pages et quinze jours de retard.» C'est ainsi qu'Achille passa du statut peu enviable de bouche-trou à celui de star ! Dans le même temps, les Américains souhaitent lancer une série de télé-dessins animés dont le héros serait Tintin. Comme Hergé ne manie pas bien l'anglais, il demande à Greg de l'accompagner à la projection du film-pilote. La lumière s'éteint, un type avec un gros T sur son tee-shirt envahit l'écran et braille: «My name is Tintin» (prononcer Tine-Tine). Hergé dit sobrement «Coupez!» et exige un scénariste qui travaille pour lui. Voilà comment en 1969, pour les Studios Belvision à Bruxelles, Greg adapte Les Sept Boules de Cristal et Le Temple du Soleil en un seul long métrage d'animation, puis écrit le scénario original d'un second film de cinéma, Tintin et le Lac aux Requins, en 1971. Directeur littéraire aux éditions Dargaud depuis 1975, Greg s'installe à New York dans le but d'y promouvoir la bande dessinée européenne, en tant que responsable du bureau américain des éditions Dargaud. Il travaille aussi aux scénarios de quelques séries télévisées, en tant que spécialiste du «pittoresque» français vu de l'Amérique... Comme il lui reste du temps libre, il écrit six polars pour Le Fleuve Noir, où il mêle ce qu'il sait de la PJ et de la police new-yorkaise. Récompensé des prix internationaux les plus prestigieux pour ses diverses productions, lauréat du Grand Prix des Arts graphiques décerné à Angoulême en 1987 et consacré «Géant de la BD» en 1992 par la Chambre belge des Experts en Bande dessinée, Greg est par ailleurs promu Chevalier des Arts et Lettres par le ministre français de la Culture François Léotard en 1988, et Chevalier de l'Ordre de Léopold par le roi Baudouin Ier de Belgique la même année, puis Officier des Arts et Lettres en 1999 par la ministre Catherine Trautmann. En 1991, Greg entame la série Colby avec Michel Blanc-Dumont et relance Comanche avec Michel Rouge chez Dargaud. En 1992, Aidans illustre un 16e album de Bernard Prince pour les éditions Blanco et Paape met en images les aventures de Johnny Congo créées pour les éditions Lefrancq. En 1995, réédition de Zig et Puce chez Glénat. En 1993, le trentième anniversaire de son héros culte Achille Talon est l’occasion de nombreuses festivités à travers toute la France et la Belgique. Dès 1994, Le Lombard réédite ses prodigieuses aventures de Bernard Prince dessinées par Hermann et de Bruno Brazil dessinées par Vance. Greg revend les droits d’Achille Talon aux éditions Dargaud en 1997, la même année où Canal+ diffuse la série animée, dont Greg a écrit l’adaptation française. En janvier 1999, le festival d’Angoulême célèbre son talent en lui consacrant une exposition rétrospective, intitulée « ABCDEF… Greg ! ». Le mois suivant, Dialogue sans bulles paraît aux éditions Dargaud : un important recueil d’entretiens recueillis et illustrés par Benoît Mouchart. Greg est décédé le 29 octobre 1999.");
        author.setPhotoUrl("https://www.bedetheque.com/media/Photos/Photo_77.jpg");
        author.setScrapUrl("https://www.bedetheque.com/auteur-77-BD-Greg.html");
        HttpEntity<Author> authorHttpEntity = new HttpEntity<>(author, null);

        // When posting this new author to /api/authors
        ResponseEntity<Author> responseEntity = this.testRestTemplate.postForEntity("/api/authors", authorHttpEntity, Author.class);
        Author createdAuthor = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createdAuthor.getNickname()).isEqualTo("Greg");
        idLastCreatedAuthor = createdAuthor.getId();
        System.out.println("ID créé = " + idLastCreatedAuthor);
    }

}