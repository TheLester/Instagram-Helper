
package com.steelkiwi.instagramhelper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Counts {

    @SerializedName("media")
    @Expose
    private Integer media;
    @SerializedName("follows")
    @Expose
    private Integer follows;
    @SerializedName("followed_by")
    @Expose
    private Integer followedBy;

    /**
     * 
     * @return
     *     The media
     */
    public Integer getMedia() {
        return media;
    }

    /**
     * 
     * @param media
     *     The media
     */
    public void setMedia(Integer media) {
        this.media = media;
    }

    /**
     * 
     * @return
     *     The follows
     */
    public Integer getFollows() {
        return follows;
    }

    /**
     * 
     * @param follows
     *     The follows
     */
    public void setFollows(Integer follows) {
        this.follows = follows;
    }

    /**
     * 
     * @return
     *     The followedBy
     */
    public Integer getFollowedBy() {
        return followedBy;
    }

    /**
     * 
     * @param followedBy
     *     The followed_by
     */
    public void setFollowedBy(Integer followedBy) {
        this.followedBy = followedBy;
    }

}
