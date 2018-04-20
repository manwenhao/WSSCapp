package com.example.chen.wsscapp.Bean;

/**
 * Created by chen on 2018/4/9.
 */

public class GridItem {

        private String path;
        private String imageView;

        public GridItem() {
            super();
        }

        public GridItem(String path, String imageView) {
            this.path = path;
            this.imageView = imageView;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getImageView() {
            return imageView;
        }

        public void setImageView(String imageView) {
            this.imageView = imageView;
        }

        @Override
        public String toString() {
            return "GridItem{" +
                    "path='" + path + '\'' +
                    ", imageView=" + imageView +
                    '}';
        }

}
