/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseHelper;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elena
 */
@Named
@RequestScoped
@Entity
@Table(name = "qa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Qa.findAll", query = "SELECT q FROM Qa q")
    , @NamedQuery(name = "Qa.findById", query = "SELECT q FROM Qa q WHERE q.id = :id")
    , @NamedQuery(name = "Qa.findByQuestion", query = "SELECT q FROM Qa q WHERE q.question = :question")
    , @NamedQuery(name = "Qa.findByRightAnswer", query = "SELECT q FROM Qa q WHERE q.rightAnswer = :rightAnswer")
    , @NamedQuery(name = "Qa.findByOptionalAnswer1", query = "SELECT q FROM Qa q WHERE q.optionalAnswer1 = :optionalAnswer1")
    , @NamedQuery(name = "Qa.findByOptionalAnswer2", query = "SELECT q FROM Qa q WHERE q.optionalAnswer2 = :optionalAnswer2")
    , @NamedQuery(name = "Qa.findByOptionalAnswer3", query = "SELECT q FROM Qa q WHERE q.optionalAnswer3 = :optionalAnswer3")
    , @NamedQuery(name = "Qa.findByMelodyRoot", query = "SELECT q FROM Qa q WHERE q.melodyRoot = :melodyRoot")
    , @NamedQuery(name = "Qa.findByPassed", query = "SELECT q FROM Qa q WHERE q.passed = :passed")
    , @NamedQuery(name = "Qa.findBySecretCode", query = "SELECT q FROM Qa q WHERE q.secretCode = :secretCode")})
public class Qa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 150)
    @Column(name = "QUESTION")
    private String question;
    @Size(max = 100)
    @Column(name = "RIGHT_ANSWER")
    private String rightAnswer;
    @Size(max = 100)
    @Column(name = "OPTIONAL_ANSWER_1")
    private String optionalAnswer1;
    @Size(max = 100)
    @Column(name = "OPTIONAL_ANSWER_2")
    private String optionalAnswer2;
    @Size(max = 100)
    @Column(name = "OPTIONAL_ANSWER_3")
    private String optionalAnswer3;
    @Size(max = 100)
    @Column(name = "MELODY_ROOT")
    private String melodyRoot;
    @Column(name = "PASSED")
    private Integer passed;
    @Size(max = 45)
    @Column(name = "SECRET_CODE")
    private String secretCode;

    public Qa() {
    }

    public Qa(Integer id) {
        this.id = id;
    }

    public Qa(String question, String rightAnswer) {
        this.question = question;
        this.rightAnswer = rightAnswer;
    }

    Qa(String secretCode, String question, String rightAnswer, String optionalAnswer1, String optionalAnswer2, String optionalAnswer3) {
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.optionalAnswer1 = optionalAnswer1;
        this.optionalAnswer2 = optionalAnswer2;
        this.optionalAnswer3 = optionalAnswer3;
        this.secretCode = secretCode;
        passed = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getOptionalAnswer1() {
        return optionalAnswer1;
    }

    public void setOptionalAnswer1(String optionalAnswer1) {
        this.optionalAnswer1 = optionalAnswer1;
    }

    public String getOptionalAnswer2() {
        return optionalAnswer2;
    }

    public void setOptionalAnswer2(String optionalAnswer2) {
        this.optionalAnswer2 = optionalAnswer2;
    }

    public String getOptionalAnswer3() {
        return optionalAnswer3;
    }

    public void setOptionalAnswer3(String optionalAnswer3) {
        this.optionalAnswer3 = optionalAnswer3;
    }

    public String getMelodyRoot() {
        return melodyRoot;
    }

    public void setMelodyRoot(String melodyRoot) {
        this.melodyRoot = melodyRoot;
    }

    public Integer getPassed() {
        return passed;
    }

    public void setPassed(Integer passed) {
        this.passed = passed;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Qa)) {
            return false;
        }
        Qa other = (Qa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseHelper.Qa[ id=" + id + " ]";
    }
    
}
