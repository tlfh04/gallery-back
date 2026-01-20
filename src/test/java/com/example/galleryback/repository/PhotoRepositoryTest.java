package com.example.galleryback.repository;


import com.example.galleryback.entity.Photo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = AutowireMode.ALL)
public class PhotoRepositoryTest {
    private final PhotoRepository photoRepository;

    @Test
    @DisplayName("사진 저장 성공")
    void save() {
        // given 필요한 데이터 설정
        Photo photo = Photo.builder()
                .title("test image")
                .description("test content")
                .imageUrl("/uploads/test.jpg")
                .build();

        // when 테스트 동작 수행
        Photo saved = photoRepository.save(photo);

        // then 결과가 일치하는지 확인
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("test image");
    }

    @Test
    @DisplayName("사진 목록 조회")
    void findAll() {
        photoRepository.save(
                Photo.builder()
                        .title("test image1")
                        .description("test content1")
                        .imageUrl("/uploads/test1.jpg")
                        .build()
        );
        photoRepository.save(
                Photo.builder()
                        .title("test image2")
                        .description("test content2")
                        .imageUrl("/uploads/test2.jpg")
                        .build()
        );

        List<Photo> photos = photoRepository.findAll();

        assertThat(photos).hasSize(2);
    }

}