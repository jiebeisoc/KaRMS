/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filter;

import entity.Employee;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author chai
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})

public class SecurityFilter implements Filter {
    FilterConfig filterConfig;
    
    private static final String CONTEXT_ROOT = "/KaRMSManagementClient";
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();        
        
        

        if(httpSession.getAttribute("isLogin") == null)
        {
            httpSession.setAttribute("isLogin", false);
        }
        
        Boolean isLogin = (Boolean)httpSession.getAttribute("isLogin");
        
        if (!excludeLoginCheck(requestServletPath)) {
        
            if (isLogin == true) {
                Employee currentEmployee = (Employee)httpSession.getAttribute("currentEmployee");
                if (checkAccessRight(requestServletPath, currentEmployee.getAccessRightEnum())) {
                    chain.doFilter(request, response);
                } else {
                    httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
                }
            } else {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
            }  
        } else {
            chain.doFilter(request, response);
        }
    }
    
    public void destroy() {
    }
    
    private Boolean checkAccessRight(String path, AccessRightEnum accessRight) {
        if (accessRight.equals(AccessRightEnum.CASHIER)) {
            if (path.equals("/customerOperation/reservationManagement.xhtml") ||
                    path.equals("/customerOperation/settlePayment.xhtml") ||
                    path.equals("/operationManagement/roomManagement.xhtml") ||
                    path.equals("/cashierOperation/browseAllFoodItems.xhtml") ||
                    path.equals("/cashierOperation/shoppingCart.xhtml") ||
                    path.equals("/cashierOperation/viewFoodOrderTransactions.xhtml")||
                    path.equals("/cashierOperation/checkout.xhtml") ||
                    path.equals("/cashierOperation/viewFoodItemDetailsFoodOrder.xhtml") ||
                    path.equals("/cashierOperation/viewFoodOrderTransactionDetails.xhtml")     
                            ){
                return true;
            }
        } else if (accessRight.equals(AccessRightEnum.MANAGER)) {
            if (path.equals("/operationManagement/foodItemManagement.xhtml") ||
                    path.equals("/operationManagement/outletManagement.xhtml") ||
                    path.equals("/operationManagement/promotionManagement.xhtml") ||
                    path.equals("/operationManagement/roomManagement.xhtml") ||
                    path.equals("/operationManagement/roomRateManagement.xhtml") ||
                    path.equals("/operationManagement/roomTypeManagement.xhtml")) {
                return true;
            }
        }
        return false;
    }
    
    private Boolean excludeLoginCheck(String path)
    {
        if(path.equals("/index.xhtml") ||
            path.equals("/accessRightError.xhtml") ||
            path.startsWith("/javax.faces.resource"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}