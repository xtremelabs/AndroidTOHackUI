package com.xtremelabs.androidtohackui.bubbles.controllers;

/**
 * Provides an interface for the bubble controller to talk to an activity that you want to pop-up a
 * bubble in.
 * 
 * <b>Usage:</b> Implement this interface for any activity that you want to have bubbles pop-up in.
 */
public interface IBubbleContainer {

    /**
     * Gets the resource id for the view that will contain the bubble.
     * 
     * @return
     */
    int getBubbleContainerId();


    /**
     * Initializes the bubble
     * 
     * @param bubbleController
     */
    void initBubble(AbstractBubbleController bubbleController);
}
