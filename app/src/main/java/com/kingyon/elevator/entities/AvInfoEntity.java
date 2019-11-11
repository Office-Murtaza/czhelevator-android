package com.kingyon.elevator.entities;

import java.util.List;

public class AvInfoEntity {

    /**
     * streams : [{"index":0,"codec_name":"h264","codec_long_name":"H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10","profile":"High","codec_type":"video","codec_time_base":"1/30","codec_tag_string":"avc1","codec_tag":"0x31637661","width":1152,"height":864,"coded_width":1152,"coded_height":864,"has_b_frames":0,"sample_aspect_ratio":"1:1","display_aspect_ratio":"4:3","pix_fmt":"yuv420p","level":40,"color_range":"tv","color_space":"bt709","color_transfer":"bt709","color_primaries":"bt709","chroma_location":"left","refs":1,"is_avc":"true","nal_length_size":"4","r_frame_rate":"15/1","avg_frame_rate":"15/1","time_base":"1/15000","start_pts":0,"start_time":"0.000000","duration_ts":96200394,"duration":"6413.359600","bit_rate":"70876","bits_per_raw_sample":"8","nb_frames":"96200","disposition":{"default":1,"dub":0,"original":0,"comment":0,"lyrics":0,"karaoke":0,"forced":0,"hearing_impaired":0,"visual_impaired":0,"clean_effects":0,"attached_pic":0,"timed_thumbnails":0},"tags":{"creation_time":"2013-01-07T12:58:08.000000Z","language":"eng","handler_name":"Video Media Handler","encoder":"AVC Coding"}},{"index":1,"codec_name":"aac","codec_long_name":"AAC (Advanced Audio Coding)","profile":"LC","codec_type":"audio","codec_time_base":"1/44100","codec_tag_string":"mp4a","codec_tag":"0x6134706d","sample_fmt":"s16p","sample_rate":"44100","channels":2,"channel_layout":"stereo","bits_per_sample":0,"r_frame_rate":"0/0","avg_frame_rate":"0/0","time_base":"1/44100","start_pts":0,"start_time":"0.000000","duration_ts":282829158,"duration":"6413.359592","bit_rate":"53585","max_bit_rate":"53438","nb_frames":"276201","disposition":{"default":1,"dub":0,"original":0,"comment":0,"lyrics":0,"karaoke":0,"forced":0,"hearing_impaired":0,"visual_impaired":0,"clean_effects":0,"attached_pic":0,"timed_thumbnails":0},"tags":{"creation_time":"2013-01-07T12:58:08.000000Z","language":"eng","handler_name":"Sound Media Handler"}}]
     * format : {"nb_streams":2,"nb_programs":0,"format_name":"mov,mp4,m4a,3gp,3g2,mj2","format_long_name":"QuickTime / MOV","start_time":"0.000000","duration":"6413.359589","size":"101416337","bit_rate":"126506","probe_score":100,"tags":{"major_brand":"mp42","minor_version":"1","compatible_brands":"M4V mp42isom","creation_time":"2013-01-07T12:58:08.000000Z"}}
     */

    private FormatBean format;
    private List<StreamsBean> streams;

    public FormatBean getFormat() {
        return format;
    }

    public void setFormat(FormatBean format) {
        this.format = format;
    }

    public List<StreamsBean> getStreams() {
        return streams;
    }

    public void setStreams(List<StreamsBean> streams) {
        this.streams = streams;
    }

    public static class FormatBean {
        /**
         * nb_streams : 2
         * nb_programs : 0
         * format_name : mov,mp4,m4a,3gp,3g2,mj2
         * format_long_name : QuickTime / MOV
         * start_time : 0.000000
         * duration : 6413.359589
         * size : 101416337
         * bit_rate : 126506
         * probe_score : 100
         * tags : {"major_brand":"mp42","minor_version":"1","compatible_brands":"M4V mp42isom","creation_time":"2013-01-07T12:58:08.000000Z"}
         */

        private int nb_streams;
        private int nb_programs;
        private String format_name;
        private String format_long_name;
        private String start_time;
        private String duration;
        private String size;
        private String bit_rate;
        private int probe_score;
        private TagsBean tags;

        public int getNb_streams() {
            return nb_streams;
        }

        public void setNb_streams(int nb_streams) {
            this.nb_streams = nb_streams;
        }

        public int getNb_programs() {
            return nb_programs;
        }

        public void setNb_programs(int nb_programs) {
            this.nb_programs = nb_programs;
        }

        public String getFormat_name() {
            return format_name;
        }

        public void setFormat_name(String format_name) {
            this.format_name = format_name;
        }

        public String getFormat_long_name() {
            return format_long_name;
        }

        public void setFormat_long_name(String format_long_name) {
            this.format_long_name = format_long_name;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getBit_rate() {
            return bit_rate;
        }

        public void setBit_rate(String bit_rate) {
            this.bit_rate = bit_rate;
        }

        public int getProbe_score() {
            return probe_score;
        }

        public void setProbe_score(int probe_score) {
            this.probe_score = probe_score;
        }

        public TagsBean getTags() {
            return tags;
        }

        public void setTags(TagsBean tags) {
            this.tags = tags;
        }

        public static class TagsBean {
            /**
             * major_brand : mp42
             * minor_version : 1
             * compatible_brands : M4V mp42isom
             * creation_time : 2013-01-07T12:58:08.000000Z
             */

            private String major_brand;
            private String minor_version;
            private String compatible_brands;
            private String creation_time;

            public String getMajor_brand() {
                return major_brand;
            }

            public void setMajor_brand(String major_brand) {
                this.major_brand = major_brand;
            }

            public String getMinor_version() {
                return minor_version;
            }

            public void setMinor_version(String minor_version) {
                this.minor_version = minor_version;
            }

            public String getCompatible_brands() {
                return compatible_brands;
            }

            public void setCompatible_brands(String compatible_brands) {
                this.compatible_brands = compatible_brands;
            }

            public String getCreation_time() {
                return creation_time;
            }

            public void setCreation_time(String creation_time) {
                this.creation_time = creation_time;
            }
        }
    }

    public static class StreamsBean {
        private String codec_name;
        private String codec_long_name;
        private String profile;
        private String codec_type;
        private String codec_time_base;
        private String codec_tag_string;
        private String codec_tag;
        private int coded_width;
        private int coded_height;

        public String getCodec_name() {
            return codec_name;
        }

        public void setCodec_name(String codec_name) {
            this.codec_name = codec_name;
        }

        public String getCodec_long_name() {
            return codec_long_name;
        }

        public void setCodec_long_name(String codec_long_name) {
            this.codec_long_name = codec_long_name;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getCodec_type() {
            return codec_type;
        }

        public void setCodec_type(String codec_type) {
            this.codec_type = codec_type;
        }

        public String getCodec_time_base() {
            return codec_time_base;
        }

        public void setCodec_time_base(String codec_time_base) {
            this.codec_time_base = codec_time_base;
        }

        public String getCodec_tag_string() {
            return codec_tag_string;
        }

        public void setCodec_tag_string(String codec_tag_string) {
            this.codec_tag_string = codec_tag_string;
        }

        public String getCodec_tag() {
            return codec_tag;
        }

        public void setCodec_tag(String codec_tag) {
            this.codec_tag = codec_tag;
        }

        public int getCoded_width() {
            return coded_width;
        }

        public void setCoded_width(int coded_width) {
            this.coded_width = coded_width;
        }

        public int getCoded_height() {
            return coded_height;
        }

        public void setCoded_height(int coded_height) {
            this.coded_height = coded_height;
        }
    }
}
