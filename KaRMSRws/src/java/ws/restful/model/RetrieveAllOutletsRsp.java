/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Outlet;
import java.util.List;

/**
 *
 * @author chai
 */
public class RetrieveAllOutletsRsp {
    
    private List<Outlet> outlets;

    public RetrieveAllOutletsRsp() {
    }

    public RetrieveAllOutletsRsp(List<Outlet> outlets) {
        this.outlets = outlets;
    }

    public List<Outlet> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<Outlet> outlets) {
        this.outlets = outlets;
    }
    
    
}
