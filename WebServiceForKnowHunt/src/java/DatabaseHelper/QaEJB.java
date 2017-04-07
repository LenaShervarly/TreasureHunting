/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseHelper;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author elena
 */
@Stateless
@Named
public class QaEJB {
    
     @PersistenceContext
     EntityManager entityManager;
     @Inject Qa qa;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
     
     public List getList() {
         return entityManager.createNamedQuery("Qa.findAll").getResultList();
     }
     
     public void create() {
         Qa qaTable = new Qa(qa.getSecretCode(), qa.getQuestion(), qa.getRightAnswer(), qa.getOptionalAnswer1(), qa.getOptionalAnswer2(), qa.getOptionalAnswer3());
         entityManager.persist(qaTable);
     }
     
     public String getQuestion(int id) {
         return ((Qa)getList().get(id)).getQuestion();
     }
}
