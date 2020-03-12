/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Member;
import java.util.List;
import javax.ejb.Local;
import util.exception.MemberNotFoundException;

/**
 *
 * @author user
 */
@Local
public interface MemberSessionBeanLocal {

    public Long createNewMember(Member newMember);

    public List<Member> retrieveAllMembers();

    public Member retrieveMemberById(long memberId);

    public Member retrieveMemberByUsername(String username) throws MemberNotFoundException;

    public void updateMember(Member memberToUpdate);

    public void deleteMember(Long memberId);

    public void addPoints(Member member, int pointsToAdd);


    
}
