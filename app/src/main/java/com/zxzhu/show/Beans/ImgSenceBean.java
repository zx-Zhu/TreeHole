package com.zxzhu.show.Beans;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/19.
 */

public class ImgSenceBean {

    /**
     * time_used : 1455
     * scenes : []
     * image_id : +zFOHDK2c1tp948KxVxOsA==
     * objects : [{"confidence":20.888,"value":"Alcazar"},{"confidence":20.87,"value":"Temple"}]
     * request_id : 1473763608,d04a67e3-0bf8-4fdf-93d2-33100ea8654b
     */

    private int time_used;
    private String image_id;
    private String request_id;
    private List<SencesBean> scenes;
    private List<ObjectsBean> objects;
    /**
     * error_message : MISSING_ARGUMENTS: image_url, image_file, image_base64
     */

    private String error_message;

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public String getImage_id() {
        return image_id;
    }



    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public List<SencesBean> getScenes() {
        return scenes;
    }

    public void setScenes(List<SencesBean> scenes) {
        this.scenes = scenes;
    }

    public List<ObjectsBean> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectsBean> objects) {
        this.objects = objects;
    }



    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public static class ObjectsBean {
        /**
         * confidence : 20.888
         * value : Alcazar
         */

        private double confidence;
        private String value;

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class SencesBean {
        /**
         * confidence : 20.888
         * value : Alcazar
         */

        private double confidence;
        private String value;

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
