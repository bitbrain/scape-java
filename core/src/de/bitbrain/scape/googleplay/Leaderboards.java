package de.bitbrain.scape.googleplay;

public enum Leaderboards {

   QUICKEST_PLAYERS_LEVEL_1("CgkIwair0J0PEAIQIQ"),
   QUICKEST_PLAYERS_LEVEL_2("CgkIwair0J0PEAIQIg"),
   QUICKEST_PLAYERS_LEVEL_3("CgkIwair0J0PEAIQIw"),
   QUICKEST_PLAYERS_LEVEL_4("CgkIwair0J0PEAIQJA"),
   QUICKEST_PLAYERS_LEVEL_5("CgkIwair0J0PEAIQJQ"),
   QUICKEST_PLAYERS_LEVEL_8("CgkIwair0J0PEAIQJg"),
   QUICKEST_PLAYERS_LEVEL_9("CgkIwair0J0PEAIQJw"),
   QUICKEST_PLAYERS_LEVEL_10("CgkIwair0J0PEAIQKA"),
   BYTE_COLLECTORS("CgkIwair0J0PEAIQKQ"),
   QUICKEST_PLAYERS_SPEEDRUN("CgkIwair0J0PEAIQAg");

   private final String googlePlayId;

   Leaderboards(String googlePlayId) {
      this.googlePlayId = googlePlayId;
   }

   public String getGooglePlayId() {
      return googlePlayId;
   }
}
