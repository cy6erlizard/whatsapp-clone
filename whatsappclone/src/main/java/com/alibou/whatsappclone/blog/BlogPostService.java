package com.alibou.whatsappclone.blog;

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
public class BlogPostService {
    
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    private final BlogPostMapper blogPostMapper;
    
    @Transactional
    public BlogPostResponse createBlogPost(BlogPostRequest request, Authentication authentication) {
        User author = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        BlogPost blogPost = blogPostMapper.toEntity(request);
        blogPost.setAuthor(author);
        
        BlogPost savedBlogPost = blogPostRepository.save(blogPost);
        return blogPostMapper.toResponse(savedBlogPost);
    }
    
    public List<BlogPostResponse> getAllPublishedPosts() {
        return blogPostRepository.findAllPublishedPosts().stream()
                .map(blogPostMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public BlogPostResponse getBlogPostById(String id) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        return blogPostMapper.toResponse(blogPost);
    }
    
    public List<BlogPostResponse> getUserBlogPosts(Authentication authentication) {
        return blogPostRepository.findByAuthorId(authentication.getName()).stream()
                .map(blogPostMapper::toResponse)
                .collect(Collectors.toList());
    }
} 