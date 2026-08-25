package org.example.knockin.service.impl;
 
import org.example.knockin.meta.entity.Region;
import org.example.knockin.meta.service.impl.RegionServiceImpl;
import org.example.knockin.meta.repository.RegionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.List;
import java.util.Optional;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
 
@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {
 
    @Mock
    private RegionRepository regionRepository;
 
    @InjectMocks
    private RegionServiceImpl regionService;
 
    @Test
    @DisplayName("지역 자식들까지 포함해서 조회 테스트")
    void findByIdInWithChildTest() {
        List<Long> ids = List.of(1L);
        Region region = mock(Region.class);
        given(regionRepository.findByIdInWithChild(ids)).willReturn(List.of(region));
 
        List<Region> result = regionService.findByIdInWithChild(ids);
 
        assertThat(result).hasSize(1);
        verify(regionRepository).findByIdInWithChild(ids);
    }
 
    @Test
    @DisplayName("ID로 지역 조회 테스트")
    void findByIdTest() {
        Region region = mock(Region.class);
        given(regionRepository.findById(1L)).willReturn(Optional.of(region));
 
        Optional<Region> result = regionService.findById(1L);
 
        assertThat(result).isPresent().contains(region);
        verify(regionRepository).findById(1L);
    }
 
    @Test
    @DisplayName("여러 지역 정보 조회 테스트")
    void findByRegionsTest() {
        List<Long> regions = List.of(1L, 2L);
        Region region = mock(Region.class);
        given(regionRepository.findByRegions(regions)).willReturn(List.of(region));
 
        List<Region> result = regionService.findByRegions(regions);
 
        assertThat(result).hasSize(1);
        verify(regionRepository).findByRegions(regions);
    }
 
    @Test
    @DisplayName("모든 지역 조회 테스트")
    void findAllTest() {
        Region region = mock(Region.class);
        given(regionRepository.findAll()).willReturn(List.of(region));
 
        List<Region> result = regionService.findAll();
 
        assertThat(result).hasSize(1);
        verify(regionRepository).findAll();
    }
}
