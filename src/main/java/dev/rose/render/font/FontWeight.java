package dev.rose.render.font;

public enum FontWeight {
   REGULAR("onest-regular"),
   MEDIUM("onest-medium"),
   SEMIBOLD("onest-semibold"),
   BOLD("onest-bold"),
   DISPLAY("unbounded-medium"),
   DISPLAY_BOLD("unbounded-bold"),
   DISPLAY_BLACK("unbounded-black");

   final String file;

   FontWeight(String file) {
      this.file = file;
   }
}
