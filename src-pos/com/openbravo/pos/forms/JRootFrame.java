//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

/* JOSE DE PAZ -JDP-
 * 05/12/2014 Generaciï¿½n de versiï¿½n 1.0
 * 01/10/2015 Se corrige para solucionar el problema de printCategoria. V.1.3.13-1
 * 29/10/2015 Se modifica un valor en Cierre de caja por el problema de acumulaciï¿½n de doble cierre V.1.3.13-02
 * 11/06/2016 V. 1.3.15 Funcionalidad para impresiï¿½n de detalle de IVA cobrado.
 *            En Cierre de Caja se agrega funcionalidad para imprimir Cierre de Caja por nï¿½mero de consecutivo
 * 13/06/2016 V. 1.3.15-01 Cambios para no mostrar PRODUCTOS con estado desactivado en PRODUCT_CAT
 * 15/06/2016 V. 1.3.15-02 Ordenamiento por TAX en detalle VENTA e IVA en reporte X y Reporte Z.   
 * 16/06/2016 V. 1.3.15-03 Cambio para impresiï¿½n de TaxName por printName()   
 * 11/07/2016 V. 1.3.16    New payment form VENTA and modifications on "CLOSED CASH: REPORTE X AND REPORTE Z"    
 * 25/08/2016 V. 1.3.17    Add "setEnabled" to button edit line, from file permission. 
 * 19/12/2016 V. 1.3.18    Solution a bug in generation Reporte X y Reporte Zeta: 
 *                         - Bad calculation in details for VENTA DEFINITIVAS and DEVOLUCIONES APLICADAS.  
 * 06/06/2017 V. 1.3.19    New funcionalitys: Delete special chars when load char from reader
 *                         Not generate receipt when payment method is equal to PREPAGO
 *                         New report for SALDO CUPO CLIENTE PREPAGO
 *                         New print ticket when payment method is equal to PREPAGO
 * 17/06/2017 V. 1.3.20    Add new report named REPORTE CLIENTE DETALLADO ACUMULADO DIARIO
 * 21/06/2017 V. 1.3.21    Add two new reports
 * 21/06/2017 V. 1.3.23    Refactoring in last three reports.
 * 21/06/2017 V. 1.3.24    Refactoring in last three reports.
 * 26/06/2017 V. 1.3.25    Add new form for new funcionality named RECARGA CUPO CLIENTE POR FECHA
 * 29/06/2017 V. 1.3.26    Add new method for save records in form RECARGA CUPO CLIENTE POR FECHA
 * 10/07/2017 V. 1.3.27    Add new field named APLICA IVA into Customers table.
 * 17/07/2017 V. 1.3.28    VENTAS CREDITO and VENTAS PREPAGO without VENTA NETA and TOTAL IVA
 *                         GROUPING for TICKETLINES.PRODUCT and PRECIO
 *                         
 * 26/07/2017 V. 1.3.32    Cambio en alerta de CUPO cuando el CLIENTE no tiene suficiente para la compra.
 * 17/08/2017 V. 1.3.33    Cambio en impresión de IVA, se agrega campos NOMOSIVA
 * 25/08/2017 V. 1.3.34-1  Corrección BUG cuando el usuario ingresa número de ticket a editar
 * 09/09/2017 V. 1.3.35    Implementación de Cupo Automático para Clientes.
 *                         Nuevo parámetro en archivo properties "recargaCupo".
 */

// FECHA        TAG   AUTOR           VERSION    DESCRIPCION
// ---------------------------------------------------------------------------------------------------------
// 27/09/2015   JDP   JOSE DE PAZ     1.3.14     Se modifica para imprimir 84 caracteres en papel tamaï¿½o carta 
// 30/09/2015   JDP   JOSE DE PAZ     1.3.14-1   Por correcciï¿½n de bug en printCategoria  



package com.openbravo.pos.forms;

import com.openbravo.pos.config.JFrmConfig;
import java.awt.BorderLayout;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import com.openbravo.pos.instance.AppMessage;
import com.openbravo.pos.instance.InstanceManager;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author  adrianromero
 */
public class JRootFrame extends javax.swing.JFrame implements AppMessage {
    
    // Gestor de que haya solo una instancia corriendo en cada maquina.
    private InstanceManager m_instmanager = null;
    
    private JRootApp m_rootapp;
    private AppProperties m_props;
    
    /** Creates new form JRootFrame */
    public JRootFrame() {
        
        initComponents();    
    }
    
    public void initFrame(AppProperties props) {
        
        m_props = props;
        
        m_rootapp = new JRootApp();
        
        if (m_rootapp.initApp(m_props)) {
            
            // Register the running application
            try {
                m_instmanager = new InstanceManager(this);
            } catch (Exception e) {
            }
        
            // Show the application
            add(m_rootapp, BorderLayout.CENTER);            
 
            try {
                this.setIconImage(ImageIO.read(JRootFrame.class.getResourceAsStream("/com/openbravo/images/favicon.png")));
            } catch (IOException e) {
            }   
           // setTitle(AppLocal.APP_NAME + " - " + AppLocal.APP_VERSION);
            setTitle(AppLocal.APP_NAME + " - " + "1.3.37"); /* 23/09/2017 SV JDP*/
            pack();
            setLocationRelativeTo(null);        
            
            setVisible(true);                        
        } else {
            new JFrmConfig(props).setVisible(true); // Show the configuration window.
        }
    }
    
    public void restoreWindow() throws RemoteException {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (getExtendedState() == JFrame.ICONIFIED) {
                    setExtendedState(JFrame.NORMAL);
                }
                requestFocus();
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        m_rootapp.tryToClose();
        
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

        System.exit(0);
        
    }//GEN-LAST:event_formWindowClosed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
