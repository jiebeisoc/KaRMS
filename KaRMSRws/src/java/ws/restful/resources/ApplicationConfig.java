/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import java.util.Set;

/**
 *
 * @author chai
 */
@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.restful.resources.CustomerResource.class);
        resources.add(ws.restful.resources.FoodItemCategoryResource.class);
        resources.add(ws.restful.resources.FoodOrderResource.class);
        resources.add(ws.restful.resources.OutletResource.class);
        resources.add(ws.restful.resources.PromotionResource.class);
        resources.add(ws.restful.resources.ReservationResource.class);
        resources.add(ws.restful.resources.RoomRateResource.class);
        resources.add(ws.restful.resources.RoomResource.class);
        resources.add(ws.restful.resources.RoomTypeResource.class);
        resources.add(ws.restful.resources.SongCategoryResource.class);
        resources.add(ws.restful.resources.SongResource.class);
    }
    
}
