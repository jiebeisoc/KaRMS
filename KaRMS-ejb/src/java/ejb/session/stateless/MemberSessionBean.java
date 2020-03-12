/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Member;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MemberNotFoundException;

/**
 *
 * @author user
 */
@Stateless
public class MemberSessionBean implements MemberSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    
    @Override
    public Long createNewMember(Member newMember)
    {
        em.persist(newMember);
        em.flush();
        
        return newMember.getMemberId();
    }
    
    
    @Override
    public List<Member> retrieveAllMembers()
    {
        Query query = em.createQuery("SELECT m FROM Member m");
        
        return query.getResultList();
        
    }
    
    
    @Override
    public Member retrieveMemberById(long memberId)
    {
        Member member = em.find(Member.class, memberId);
        
        return member;
    }
    
    @Override
    public Member retrieveMemberByUsername(String username) throws MemberNotFoundException 
    {
        Query query = em.createQuery("SELECT m FROM Member m WHERE m.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try 
        {
            return (Member)query.getSingleResult();
        } catch (NoResultException ex)
        {
            throw new MemberNotFoundException("Member username" + username + "does not exist!");
            
        }
        
    }
    
    
    @Override
    public void updateMember(Member memberToUpdate)
    {
        em.merge(memberToUpdate);
        em.flush();
    }
    
    
    @Override
    public void deleteMember(Long memberId)
    {
        Member memberToDelete = retrieveMemberById(memberId);
        
        em.remove(memberToDelete);
    }
    
    
    @Override
    public void addPoints(Member member, int pointsToAdd)
    {
        member.setPoints(member.getPoints() + pointsToAdd);
    }
    
    public void redeemPoints(Member member,int pointsToDeduct)
    {
        member.setPoints(member.getPoints() - pointsToDeduct);
    }
    
    
}
