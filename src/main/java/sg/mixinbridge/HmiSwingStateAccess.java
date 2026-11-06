package sg.mixinbridge;

public interface HmiSwingStateAccess {
   int hMI5_0$getSwingCount();

   void hMI5_0$resetMainHandSwing(boolean var1);

   boolean hMI5_0$getOInteract();

   float hMI5_0$getMainHandSwingProgress(float var1);

   float hMI5_0$getOffHandSwingProgress(float var1);

   boolean hMI5_0$getMHandEvent();

   boolean hMI5_0$getMInteract();

   void hMI5_0$resetOffHandSwing(boolean var1);

   boolean hMI5_0$getBlockBreak();

   boolean hMI5_0$getOHandEvent();
}
