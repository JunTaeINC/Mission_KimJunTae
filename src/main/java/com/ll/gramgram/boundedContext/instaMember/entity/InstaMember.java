package com.ll.gramgram.boundedContext.instaMember.entity;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class InstaMember extends InstaMemberBase {

    @Column(unique = true)
    private String username;
    @OneToMany(mappedBy = "fromInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> fromLikeablePeople = new ArrayList<>();

    @OneToMany(mappedBy = "toInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    // 클래스에 빌더가 적혀있고 private List<LikeablePerson> toLikeablePeople = new ArrayList<>(); 이렇게 하면 다날라간다.
    private List<LikeablePerson> toLikeablePeople = new ArrayList<>();

    @OneToMany(mappedBy = "instaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc")
    @Builder.Default
    private List<InstaMemberSnapshot> instaMemberSnapshots = new ArrayList<>();

    public void addFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.add(0, likeablePerson);
    }

    public void addToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.add(0, likeablePerson);
    }

    public void removeFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.removeIf(e -> e.equals(likeablePerson));
    }

    public void removeToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.removeIf(e -> e.equals(likeablePerson));
    }

    public String getGenderDisplayName() {
        return switch (gender) {
            case "W" -> "여성";
            default -> "남성";
        };
    }

    public void updateGender(String gender) {
        String oldGender = this.gender;

        boolean oldIsNull = this.gender == null;

        if (!this.gender.equals(gender)) {
            getFromLikeablePeople()
                    .stream()
                    .forEach(likeablePerson -> {
                        InstaMember toInstamember = likeablePerson.getToInstaMember();
                        toInstamember.decreaseLikesCount(oldGender, likeablePerson.getAttractiveTypeCode());
                        toInstamember.increaseLikesCount(gender, likeablePerson.getAttractiveTypeCode());
                    });
            this.gender = gender;
        }

        if (!oldIsNull) saveSnapshot();
    }

    public void increaseLikesCount(String gender, int attractiveTypeCode) {
        if (gender.equals("W") && attractiveTypeCode == 1) likesCountByGenderWomanAndAttractiveTypeCode1++;
        if (gender.equals("W") && attractiveTypeCode == 2) likesCountByGenderWomanAndAttractiveTypeCode2++;
        if (gender.equals("W") && attractiveTypeCode == 3) likesCountByGenderWomanAndAttractiveTypeCode3++;
        if (gender.equals("M") && attractiveTypeCode == 1) likesCountByGenderManAndAttractiveTypeCode1++;
        if (gender.equals("M") && attractiveTypeCode == 2) likesCountByGenderManAndAttractiveTypeCode2++;
        if (gender.equals("M") && attractiveTypeCode == 3) likesCountByGenderManAndAttractiveTypeCode3++;

        saveSnapshot();
    }

    public void decreaseLikesCount(String gender, int attractiveTypeCode) {
        if (gender.equals("W") && attractiveTypeCode == 1) likesCountByGenderWomanAndAttractiveTypeCode1--;
        if (gender.equals("W") && attractiveTypeCode == 2) likesCountByGenderWomanAndAttractiveTypeCode2--;
        if (gender.equals("W") && attractiveTypeCode == 3) likesCountByGenderWomanAndAttractiveTypeCode3--;
        if (gender.equals("M") && attractiveTypeCode == 1) likesCountByGenderManAndAttractiveTypeCode1--;
        if (gender.equals("M") && attractiveTypeCode == 2) likesCountByGenderManAndAttractiveTypeCode2--;
        if (gender.equals("M") && attractiveTypeCode == 3) likesCountByGenderManAndAttractiveTypeCode3--;

        saveSnapshot();
    }

    public void saveSnapshot() {
        InstaMemberSnapshot instaMemberSnapshot = InstaMemberSnapshot.builder()
                .instaMember(this)
                .username(username)
                .likesCountByGenderWomanAndAttractiveTypeCode1(likesCountByGenderWomanAndAttractiveTypeCode1)
                .likesCountByGenderWomanAndAttractiveTypeCode2(likesCountByGenderWomanAndAttractiveTypeCode2)
                .likesCountByGenderWomanAndAttractiveTypeCode3(likesCountByGenderWomanAndAttractiveTypeCode3)
                .likesCountByGenderManAndAttractiveTypeCode1(likesCountByGenderManAndAttractiveTypeCode1)
                .likesCountByGenderManAndAttractiveTypeCode2(likesCountByGenderManAndAttractiveTypeCode2)
                .likesCountByGenderManAndAttractiveTypeCode3(likesCountByGenderManAndAttractiveTypeCode3)
                .build();

        instaMemberSnapshots.add(instaMemberSnapshot);
    }
}