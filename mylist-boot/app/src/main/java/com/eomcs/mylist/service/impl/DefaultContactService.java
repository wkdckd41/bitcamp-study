package com.eomcs.mylist.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eomcs.mylist.dao.ContactDao;
import com.eomcs.mylist.domain.Contact;
import com.eomcs.mylist.domain.ContactTel;
import com.eomcs.mylist.service.ContactService;

@Service
public class DefaultContactService implements ContactService {

  @Autowired
  ContactDao contactDao;

  @Override
  @Transactional // 다음 메서드는 트랜잭션 안에서 실행하도록 설정한다.
  public int add(Contact contact) {
    contactDao.insert(contact);
    for (ContactTel tel : contact.getTels()) {
      tel.setContactNo(contact.getNo()); // 전화번호 입력 전에 자동 생성된 연락처 번호를 설정한다.
      contactDao.insertTel(tel);
    }
    return 1;
  }

  @Override
  public List<Contact> list() {
    return contactDao.findAll();
  }

  @Override
  public Contact get(int no) {
    return contactDao.findByNo(no);
  }

  @Override
  @Transactional
  public int update(Contact contact) {
    int count = contactDao.update(contact);
    if (count > 0) {
      contactDao.deleteTelByContactNo(contact.getNo()); // 전화번호 변경 전에 기존 전화번호를 모두 삭제한다.
      for (ContactTel tel : contact.getTels()) {
        contactDao.insertTel(tel); // 전화번호 객체에 안에 이미 연락처 번호가 저장되어 있다.
      }
    }
    return count;
  }

  @Override
  @Transactional
  public int delete(int no) {
    contactDao.deleteTelByContactNo(no);
    return contactDao.delete(no);
  }
}







