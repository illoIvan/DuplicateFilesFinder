package com.illoivan.duplicatefiles.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.illoivan.duplicatefiles.utils.LoggerUtils;
import com.illoivan.duplicatefiles.utils.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MP3Metadata implements CustomMetadata{

    private String genre;
    private String album;
    private String releaseDate;
    private String artist;
    private String audioCompressor;
    private String audioChannelType;
    private String audioSampleRate;
    private int channels;
    private String title;
    private String albumArtist;
    private long duration;
    private String contentType;
    private String formatTime;

    public MP3Metadata() {}
    
    public MP3Metadata(CustomFile customFile) {  
       loadMetadata(customFile);
    }

    public void loadMetadata(CustomFile customFile) {
    	File file = customFile.getFile();
        InputStream inputStream = customFile.readStream();

        if(inputStream == null) {
        	LoggerUtils.error("inputStream null "+customFile.getFileName(),null);
        	customFile.setError("inputStream null");
        	return;
        }
        
        Parser parser = new Mp3Parser();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        ContentHandler contentHandler = new DefaultHandler();
        
        try {
            parser.parse(inputStream, contentHandler, metadata, context);
        } catch (IOException e) {
            LoggerUtils.error("Could not read file: "+file.toString(),e);
            customFile.setError("Could not read file: "+file.toString());
        } catch (SAXException e) {
        	LoggerUtils.error("The file could not be processed: "+file.toString(),e);
        	customFile.setError("The file could not be processed: "+file.toString());
        } catch (TikaException e) {
        	LoggerUtils.error("The file could not be parsed: "+file.toString(),e);
        	customFile.setError("The file could not be parsed: "+file.toString());
        }finally{
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                	LoggerUtils.error("The file could not be closed: "+file.toString(),e);
                	customFile.setError("The file could not be closed: "+file.toString());
                }
            } 
        }

        if(!customFile.getError().isEmpty()) {
        	return;
        }
        
        Map<String,String> properties = new HashMap<>();
        for (String property : metadata.names()) {
            properties.put(property, metadata.get(property));
        }
        
        fillMetadata(properties);
    }

    public void fillMetadata(Map<String,String> properties) {
        /*
        File Metadata MP3:
            xmpDM:genre: Genre
            xmpDM:album: album
            xmpDM:trackNumber: 1
            xmpDM:releaseDate: 2023
            xmpDM:discNumber: 1
            xmpDM:artist: Artist
            dc:creator: Creator
            xmpDM:audioCompressor: MP3
            xmpDM:audioChannelType: Stereo
            version: MPEG 3 Layer III Version 1
            xmpDM:logComment: 
            xmpDM:audioSampleRate: 44100
            channels: 2
            dc:title: Title
            xmpDM:albumArtist: AlbumArtist
            xmpDM:duration: 164.0832977294922
            Content-Type: audio/mpeg
            samplerate: 44100
        */
        properties.forEach((key, value) -> {
            switch (key) {
                case "xmpDM:genre":
                    this.genre = value;
                    break;
                case "xmpDM:album":
                    this.album = value;
                    break;
                case "xmpDM:releaseDate":
                    this.releaseDate = value;
                    break;
                case "xmpDM:artist":
                    this.artist = value;
                    break;
                case "xmpDM:audioCompressor":
                    this.audioCompressor = value;
                    break;
                case "xmpDM:audioChannelType":
                    this.audioChannelType = value;
                    break;
                case "xmpDM:audioSampleRate":
                    this.audioSampleRate = value;
                    break;
                case "xmpDM:albumArtist":
                    this.albumArtist = value;
                    break;
                case "xmpDM:duration":
                    this.duration = (long) Float.parseFloat(value);
                    break;
                case "dc:title":
                    this.title = value;
                    break;
                case "channels":
                    this.channels = Integer.parseInt(value);
                    break;
                case "Content-Type":
                    this.contentType = value;
                    break;
                case "xmpDM:trackNumber","xmpDM:discNumber","dc:creator","version","xmpDM:logComment","samplerate":
                    break;
                default:
                	LoggerUtils.error("No metadata found for key: "+key,null);
            }
        });
        this.formatTime = Utils.getFormatedDate(duration);
    }

    @Override
    public String toString() {
        return "MP3Metadata [genre=" + genre + ", album=" + album + ", releaseDate=" + releaseDate + ", artist=" + artist + ", audioCompressor=" + audioCompressor + ", audioChannelType=" + audioChannelType + ", audioSampleRate="
                + audioSampleRate + ", channels=" + channels + ", title=" + title + ", albumArtist=" + albumArtist + ", duration=" + duration + ", contentType=" + contentType + ", formatTime=" + formatTime + "]";
    }

    @Override
    public String getDescription() {
        String newLine = System.lineSeparator();
        StringBuilder str = new StringBuilder();
        str.append("Title="+title+newLine);
        str.append("Duration="+formatTime+newLine);
        str.append("Genre="+genre+newLine);
        str.append("Album="+album+newLine);
        str.append("Artist="+artist+newLine);
        str.append("AudioCompressor="+audioCompressor+newLine);
        return str.toString();
    }


}
