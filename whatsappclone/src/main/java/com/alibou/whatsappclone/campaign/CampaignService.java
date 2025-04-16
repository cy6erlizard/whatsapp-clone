package com.alibou.whatsappclone.campaign;

import com.alibou.whatsappclone.user.User;
import com.alibou.whatsappclone.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {
    
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CampaignMapper campaignMapper;
    
    @Transactional
    public CampaignResponse createCampaign(CampaignRequest request, Authentication authentication) {
        User creator = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Campaign campaign = campaignMapper.toEntity(request);
        campaign.setCreator(creator);
        
        Campaign savedCampaign = campaignRepository.save(campaign);
        return campaignMapper.toResponse(savedCampaign);
    }
    
    public List<CampaignResponse> getAllActiveCampaigns() {
        return campaignRepository.findAllActiveCampaigns().stream()
                .map(campaignMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public CampaignResponse getCampaignById(String id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        return campaignMapper.toResponse(campaign);
    }
    
    public List<CampaignResponse> getUserCampaigns(Authentication authentication) {
        return campaignRepository.findByCreatorId(authentication.getName()).stream()
                .map(campaignMapper::toResponse)
                .collect(Collectors.toList());
    }
} 