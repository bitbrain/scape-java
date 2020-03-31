package de.bitbrain.scape.googleplay;

public enum Achievements {

   // Gather all bytes of a single level
   FULL_HOUSE("CgkIwair0J0PEAIQAQ"),

   // Die for the first time
   YOU_DIED("CgkIwair0J0PEAIQBA"),

   // Collect 1000 bytes
   BYTE_HUNTER("CgkIwair0J0PEAIQBQ"),

   // Collect 10.000 bytes
   BYTE_HOARDER("CgkIwair0J0PEAIQBg"),

   // Collect 0 bytes within a level
   ZERO_BYTE_EXCEPTION("CgkIwair0J0PEAIQBw"),

   // Jump 10.000 times
   THE_JUMPER("CgkIwair0J0PEAIQCQ"),

   // Die 1000 times
   THE_CHOSEN_UNDEAD("CgkIwair0J0PEAIQCw"),

   // Complete all levels sequentially without dying
   SCAPE_ARTIST("CgkIwair0J0PEAIQCg"),

   // Complete all levels without dying and all bytes collected
   MR_ROBOT("CgkIwair0J0PEAIQCA");

   private final String googlePlayId;

   Achievements(String googlePlayId) {
      this.googlePlayId = googlePlayId;
   }

   public String getGooglePlayId() {
      return googlePlayId;
   }

}
