package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> findByToInstaMember(InstaMember toInstaMember) {
        return notificationRepository.findByToInstaMemberOrderByIdDesc(toInstaMember);
    }

    @Transactional
    public RsData<Notification> makeLike(LikeablePerson likeablePerson) {
        return make(likeablePerson, "LIKE", 0, likeablePerson.getFromInstaMember().getGender());
    }

    @Transactional
    public RsData<Notification> makeModifyAttractiveType(LikeablePerson likeablePerson, int oldAttractiveTypeCode) {
        return make(likeablePerson, "MODIFY_ATTRACTIVE_TYPE", oldAttractiveTypeCode, likeablePerson.getFromInstaMember().getGender());
    }

    private RsData<Notification> make(LikeablePerson likeablePerson, String typeCode, int oldAttractiveTypeCode, String oldGender) {
        Notification notification = Notification
                .builder()
                .toInstaMember(likeablePerson.getToInstaMember())
                .fromInstaMember(likeablePerson.getFromInstaMember())
                .typeCode(typeCode)
                .oldGender(oldGender)
                .newGender(likeablePerson.getFromInstaMember().getGender())
                .oldAttractiveTypeCode(oldAttractiveTypeCode)
                .newAttractiveTypeCode(likeablePerson.getAttractiveTypeCode())
                .build();
        notificationRepository.save(notification);

        return RsData.of("S-1", "알림 메세지가 생성되었습니다.", notification);
    }

    public List<Notification> findByToInstaMember_username(String username) {
        return notificationRepository.findByToInstaMember_usernameOrderByIdDesc(username);
    }

    @Transactional
    public RsData markAsRead(List<Notification> notifications) {

        notifications
                .stream()
                .filter(notification -> !notification.isRead())
                .forEach(notification -> notification.markAsRead());

        return RsData.of("S-1", "읽음 처리가 완료되었습니다.");
    }

    public boolean countUnreadNotificationsByToInstaMember(InstaMember instaMember) {
        return notificationRepository.countByToInstaMemberAndReadDateIsNull(instaMember) > 0;
    }
}
