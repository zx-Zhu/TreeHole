package com.zxzhu.show.Beans;

/**
 * Created by zxzhu on 2017/8/20.
 */

public class SquareBean {

    /**
     * description : 123
     * content : {"name":"zzx_pci_1503193891283","url":"http://ac-xKo7tm6a.clouddn.com/Y4SQY4xJ48vvB9UzVIH1DxJOrWVfHGLSyH04vZG3","mime_type":"application/octet-stream","bucket":"xKo7tm6a","metaData":{"size":405243,"owner":"59986d6d61ff4b0058e111b9","_name":"zzx_pci_1503193891283","_checksum":"59d847848e1cdfcad65b88b864045c3b"},"objectId":"5998eb24570c35006071ff7d","createdAt":"2017-08-20T01:51:32.136Z","updatedAt":"2017-08-20T01:51:32.136Z"}
     * type : 1
     * voice : {"name":"zzx_voice_1503193891283","url":"http://ac-xKo7tm6a.clouddn.com/3AnohN511vVvskAXTJugXcRoQoYFvkwjP7NIyz1e","mime_type":"application/octet-stream","bucket":"xKo7tm6a","metaData":{"size":58240,"owner":"59986d6d61ff4b0058e111b9","_name":"zzx_voice_1503193891283","_checksum":"f1dd3b1e1cdf3469314271ce780eb7d8"},"objectId":"5998eb24128fe10054c3f004","createdAt":"2017-08-20T01:51:32.183Z","updatedAt":"2017-08-20T01:51:32.183Z"}
     * picMini : {"name":"miniPic_zzx_pci_1503193891283","url":"http://ac-xKo7tm6a.clouddn.com/fAbcjDz7AisLJf5QZ0QC1iMQYR0Afz0scOyWHmSj","mime_type":"application/octet-stream","bucket":"xKo7tm6a","metaData":{"size":59421,"owner":"59986d6d61ff4b0058e111b9","_name":"miniPic_zzx_pci_1503193891283","_checksum":"08ac27b776319c1710e4da60f940e4e2"},"objectId":"5998eb24128fe10054c3f003","createdAt":"2017-08-20T01:51:32.176Z","updatedAt":"2017-08-20T01:51:32.176Z"}
     * objectId : 5998eb27a0bb9f005714ad9f
     * createdAt : 2017-08-20T01:51:35.263Z
     * updatedAt : 2017-08-20T01:51:35.263Z
     */

    private int description;
    private ContentBean content;
    private int type;
    private VoiceBean voice;
    private PicMiniBean picMini;
    private String objectId;
    private String createdAt;
    private String updatedAt;

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public VoiceBean getVoice() {
        return voice;
    }

    public void setVoice(VoiceBean voice) {
        this.voice = voice;
    }

    public PicMiniBean getPicMini() {
        return picMini;
    }

    public void setPicMini(PicMiniBean picMini) {
        this.picMini = picMini;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class ContentBean {
        /**
         * name : zzx_pci_1503193891283
         * url : http://ac-xKo7tm6a.clouddn.com/Y4SQY4xJ48vvB9UzVIH1DxJOrWVfHGLSyH04vZG3
         * mime_type : application/octet-stream
         * bucket : xKo7tm6a
         * metaData : {"size":405243,"owner":"59986d6d61ff4b0058e111b9","_name":"zzx_pci_1503193891283","_checksum":"59d847848e1cdfcad65b88b864045c3b"}
         * objectId : 5998eb24570c35006071ff7d
         * createdAt : 2017-08-20T01:51:32.136Z
         * updatedAt : 2017-08-20T01:51:32.136Z
         */

        private String name;
        private String url;
        private String mime_type;
        private String bucket;
        private MetaDataBean metaData;
        private String objectId;
        private String createdAt;
        private String updatedAt;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMime_type() {
            return mime_type;
        }

        public void setMime_type(String mime_type) {
            this.mime_type = mime_type;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public MetaDataBean getMetaData() {
            return metaData;
        }

        public void setMetaData(MetaDataBean metaData) {
            this.metaData = metaData;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public static class MetaDataBean {
            /**
             * size : 405243
             * owner : 59986d6d61ff4b0058e111b9
             * _name : zzx_pci_1503193891283
             * _checksum : 59d847848e1cdfcad65b88b864045c3b
             */

            private int size;
            private String owner;
            private String _name;
            private String _checksum;

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String get_name() {
                return _name;
            }

            public void set_name(String _name) {
                this._name = _name;
            }

            public String get_checksum() {
                return _checksum;
            }

            public void set_checksum(String _checksum) {
                this._checksum = _checksum;
            }
        }
    }

    public static class VoiceBean {
        /**
         * name : zzx_voice_1503193891283
         * url : http://ac-xKo7tm6a.clouddn.com/3AnohN511vVvskAXTJugXcRoQoYFvkwjP7NIyz1e
         * mime_type : application/octet-stream
         * bucket : xKo7tm6a
         * metaData : {"size":58240,"owner":"59986d6d61ff4b0058e111b9","_name":"zzx_voice_1503193891283","_checksum":"f1dd3b1e1cdf3469314271ce780eb7d8"}
         * objectId : 5998eb24128fe10054c3f004
         * createdAt : 2017-08-20T01:51:32.183Z
         * updatedAt : 2017-08-20T01:51:32.183Z
         */

        private String name;
        private String url;
        private String mime_type;
        private String bucket;
        private MetaDataBeanX metaData;
        private String objectId;
        private String createdAt;
        private String updatedAt;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMime_type() {
            return mime_type;
        }

        public void setMime_type(String mime_type) {
            this.mime_type = mime_type;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public MetaDataBeanX getMetaData() {
            return metaData;
        }

        public void setMetaData(MetaDataBeanX metaData) {
            this.metaData = metaData;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public static class MetaDataBeanX {
            /**
             * size : 58240
             * owner : 59986d6d61ff4b0058e111b9
             * _name : zzx_voice_1503193891283
             * _checksum : f1dd3b1e1cdf3469314271ce780eb7d8
             */

            private int size;
            private String owner;
            private String _name;
            private String _checksum;

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String get_name() {
                return _name;
            }

            public void set_name(String _name) {
                this._name = _name;
            }

            public String get_checksum() {
                return _checksum;
            }

            public void set_checksum(String _checksum) {
                this._checksum = _checksum;
            }
        }
    }

    public static class PicMiniBean {
        /**
         * name : miniPic_zzx_pci_1503193891283
         * url : http://ac-xKo7tm6a.clouddn.com/fAbcjDz7AisLJf5QZ0QC1iMQYR0Afz0scOyWHmSj
         * mime_type : application/octet-stream
         * bucket : xKo7tm6a
         * metaData : {"size":59421,"owner":"59986d6d61ff4b0058e111b9","_name":"miniPic_zzx_pci_1503193891283","_checksum":"08ac27b776319c1710e4da60f940e4e2"}
         * objectId : 5998eb24128fe10054c3f003
         * createdAt : 2017-08-20T01:51:32.176Z
         * updatedAt : 2017-08-20T01:51:32.176Z
         */

        private String name;
        private String url;
        private String mime_type;
        private String bucket;
        private MetaDataBeanXX metaData;
        private String objectId;
        private String createdAt;
        private String updatedAt;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMime_type() {
            return mime_type;
        }

        public void setMime_type(String mime_type) {
            this.mime_type = mime_type;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public MetaDataBeanXX getMetaData() {
            return metaData;
        }

        public void setMetaData(MetaDataBeanXX metaData) {
            this.metaData = metaData;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public static class MetaDataBeanXX {
            /**
             * size : 59421
             * owner : 59986d6d61ff4b0058e111b9
             * _name : miniPic_zzx_pci_1503193891283
             * _checksum : 08ac27b776319c1710e4da60f940e4e2
             */

            private int size;
            private String owner;
            private String _name;
            private String _checksum;

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String get_name() {
                return _name;
            }

            public void set_name(String _name) {
                this._name = _name;
            }

            public String get_checksum() {
                return _checksum;
            }

            public void set_checksum(String _checksum) {
                this._checksum = _checksum;
            }
        }
    }
}
