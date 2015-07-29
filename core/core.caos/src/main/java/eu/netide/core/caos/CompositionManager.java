package eu.netide.core.caos;

import eu.netide.core.api.FlowModEventArgs;
import eu.netide.core.api.IShimManager;
import eu.netide.core.api.PacketInEventArgs;

/**
 * Created by timvi on 25.06.2015.
 */
public class CompositionManager implements ICompositionManager {

    private IShimManager shimManager;

    public void Start() {
        // TODO read specification, startup subapplications
        System.out.println("CompositionManager started.");
    }

    public void Stop() {
        System.out.println("CompositionManager stopped.");
    }

    public void OnPacketIn(PacketInEventArgs args) {
        System.out.println("Packet-In occurred!");
    }

    public void OnFlowMod(FlowModEventArgs args) {
        System.out.println("Flow-Mod occurred!");
    }

    public void setShimManager(IShimManager manager) {
        shimManager = manager;
    }

    public IShimManager getShimManager() {
        return shimManager;
    }
}