package me.surge.mixins;

import me.surge.toasts.AdvancedToast;
import me.surge.toasts.AdvancedToastManager;
import net.minecraft.client.toast.AdvancementToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author surge
 * @since 02/07/2023
 */
@Mixin(ToastManager.class)
public class MixinToastManager {

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    public void hookAdd(Toast toast, CallbackInfo ci) {
        if (toast instanceof AdvancementToast) {
            AdvancedToastManager.add(new AdvancedToast(((IAdvancementToast) toast).getAdvancement()));

            // prevent normal handling
            ci.cancel();
        }
    }

}
