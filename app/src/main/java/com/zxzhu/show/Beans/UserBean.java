package com.zxzhu.show.Beans;

/**
 * Created by zxzhu on 2017/8/20.
 */

public class UserBean {

    /**
     * nickname : zzx
     * username : zzx
     * emailVerified : false
     * head : {"name":"zzx","url":"http://ac-xKo7tm6a.clouddn.com/O7X18gGDnLrDZY17vZLjzo3pyc5Rqk9lLw8eEbAk","mime_type":"application/octet-stream","bucket":"xKo7tm6a","metaData":{"size":165620,"owner":"59986d36a22b9df82f95c70e","_name":"zzx","_checksum":"5da4806a1525b5fcbf12cc4e249f1565"},"objectId":"59986d6c1b69e600584487d0","createdAt":"2017-08-19T16:55:08.428Z","updatedAt":"2017-08-19T16:55:08.428Z"}
     * mobilePhoneNumber : 15533538378
     * readme : null
     * mobilePhoneVerified : false
     * objectId : 59986d6d61ff4b0058e111b9
     * createdAt : 2017-08-19T16:55:09.599Z
     * updatedAt : 2017-08-19T16:55:09.599Z
     */

    private String nickname;
    private String username;
    private boolean emailVerified;
    private HeadBean head;
    private String mobilePhoneNumber;
    private Object readme;
    private boolean mobilePhoneVerified;
    private String objectId;
    private String createdAt;
    private String updatedAt;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
        this.head = head;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public Object getReadme() {
        return readme;
    }

    public void setReadme(Object readme) {
        this.readme = readme;
    }

    public boolean isMobilePhoneVerified() {
        return mobilePhoneVerified;
    }

    public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
        this.mobilePhoneVerified = mobilePhoneVerified;
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

    public static class HeadBean {
        /**
         * name : zzx
         * url : http://ac-xKo7tm6a.clouddn.com/O7X18gGDnLrDZY17vZLjzo3pyc5Rqk9lLw8eEbAk
         * mime_type : application/octet-stream
         * bucket : xKo7tm6a
         * metaData : {"size":165620,"owner":"59986d36a22b9df82f95c70e","_name":"zzx","_checksum":"5da4806a1525b5fcbf12cc4e249f1565"}
         * objectId : 59986d6c1b69e600584487d0
         * createdAt : 2017-08-19T16:55:08.428Z
         * updatedAt : 2017-08-19T16:55:08.428Z
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
             * size : 165620
             * owner : 59986d36a22b9df82f95c70e
             * _name : zzx
             * _checksum : 5da4806a1525b5fcbf12cc4e249f1565
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
