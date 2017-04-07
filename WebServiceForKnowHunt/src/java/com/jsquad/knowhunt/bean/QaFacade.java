/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsquad.knowhunt.bean;

import DatabaseHelper.Qa;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author elena
 */
@Stateless
public class QaFacade extends AbstractFacade<Qa> {

    @PersistenceContext(unitName = "WebServiceForKnowHuntPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QaFacade() {
        super(Qa.class);
    }
    
}
