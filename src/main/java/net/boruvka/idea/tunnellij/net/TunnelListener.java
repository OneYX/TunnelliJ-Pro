package net.boruvka.idea.tunnellij.net;

/**
 * @author boruvka
 * @since
 */
public interface TunnelListener {

    default void newCall(Call call) {}

    default void endCall(Call call) {}

    default void tunnelStarted() {}

    default void tunnelStopped() {}

}
