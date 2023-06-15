package fr.ptut.etron.utils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Config {
    public static final String WINDOW_TITLE = "E-Tron";
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int WINDOW_WIDTH = (int)(screenSize.getWidth());
    public static final int WINDOW_HEIGHT =(int)(screenSize.getHeight()) ;
    public static final Color WIN_BACKGROUND_COLOR = new Color(68, 189, 50);
    public static final int TILE_SIZE = 32;
    public static final int MAX_PLAYERS = 50;
    public static final int BOTS_COUNT = 5;
    public static final double CITY_CONCENTRATION = 0.02;
    public static final int NB_PIX_CASE_WIDTH = (WINDOW_WIDTH-200)/100;
    public static final int NB_PIX_CASE_HEIGHT = (WINDOW_HEIGHT-200)/100;
    public static final int MAP_HEIGHT = 100;
    public static final int MAP_WIDTH = 100;

    public static final List<String> USERNAMES = Arrays.asList(
            "TaléCrampté",
            "Christian7",
            "LaFleurDeLys",
            "Cathy",
            "LapinCretin",
            "Josette",
            "CarotteMagique",
            "MonsieurPantoufle",
            "PetitPoisson",
            "LeDingue",
            "LeFilsDeGreg",
            "OuaiCestGreg",
            "VincentTimetre",
            "PoissonRouge",
            "BebeDino",
            "Verdoze",
            "JaiToutCasse",
            "Sinsoh",
            "AntoineDeNovembre",
            "GrosNounours",
            "BananeVolante",
            "PimentVert",
            "PetiteGrenouille",
            "ZorroDesTempsModernes",
            "PuddingSucre",
            "MonsieurPatate",
            "PourToutN",
            "PrudHommosaure",
            "TépanetBaptiste",
            "Bernouille",
            "FeeClochette",
            "PouletFarceur",
            "MonsieurBobo",
            "ChatMignon",
            "LeRigolo",
            "GrenadineSucre",
            "GateauAuxFruits",
            "PetitBateau",
            "BiscuitMagique",
            "ABarEtBBar",
            "YoungGZUS",
            "Mimix2000",
            "Amouranth",
            "Pinocchio",
            "Saliha",
            "Fabienne",
            "GrosMinet",
            "VilainMatou",
            "PitiChat",
            "CaptnCrack",
            "ReineG",
            "TeteDeNoeud",
            "ChouetteHibou",
            "PoupeeDeChiffon",
            "QuaranteNeuf3",
            "Monique",
            "CocoLapin",
            "CamelCase",
            "ZebreEnFolie",
            "BambouVert",
            "PetitChaperonRouge",
            "PingouinMalin",
            "LAsDuVolant",
            "BlancDeDinde",
            "PoulePondeuse",
            "Laurence",
            "JugaBonitow"
    );
}
