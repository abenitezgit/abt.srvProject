/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eco.ftp;

import it.sauronsoftware.jave.FFMPEGLocator;

/**
 *
 * @author Hernanchi
 */
public class FFMPEGCustomLocator extends FFMPEGLocator {


    private String location= null;
    
    public FFMPEGCustomLocator(String location) {
        this.location = location;
    }
    
    @Override
    protected String getFFMPEGExecutablePath() {
        return location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
}
