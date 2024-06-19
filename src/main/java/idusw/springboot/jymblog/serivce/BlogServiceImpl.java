package idusw.springboot.jymblog.serivce;

import idusw.springboot.jymblog.entity.BlogEntity;
import idusw.springboot.jymblog.entity.MemberEntity;
import idusw.springboot.jymblog.model.BlogDto;
import idusw.springboot.jymblog.repository.BlogRepository;
import idusw.springboot.jymblog.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    final BlogRepository blogRepository;
    final MemberRepository memberRepository;
    public BlogServiceImpl(BlogRepository blogRepository, MemberRepository memberRepository){
        this.blogRepository = blogRepository;
        this.memberRepository = memberRepository;
    }
    @Override
    public int create(BlogDto dto) {
        blogRepository.save(dtoToEntity(dto)); // insert into blog values ....;
        return 0;
    }

    @Override
    @Transactional
    public BlogDto read(BlogDto dto) { //BlogDto = BLogEntity + MemberEntity
        Optional<BlogEntity> blogEntityOptional = blogRepository.findById(dto.getIdx());
        // Optional<BlogEntity> entity = blogRepository.findByIdx(dto); 위와 기능이 비슷해 둘중 하나 사용하면 됨
        Optional<MemberEntity> memberEntityOptional =
                memberRepository.findByIdx(blogEntityOptional.get().getBlogger().getIdx());
        return entityToDto(blogEntityOptional.get(), memberEntityOptional.get());
    }

    @Override
    public List<BlogDto> readList() {
        //DTO : Controller, DTO/Entity : Service, Entiry : Repository
        List<BlogEntity> blogEntityList = blogRepository.findAll(); // select * from blog;
        List<BlogDto> blogDtoList = new ArrayList<>();
        for(BlogEntity blogEntity : blogEntityList) { // JCF & Builder Pattern
            MemberEntity memberEntity = MemberEntity.builder()
                    .idx(blogEntity.getBlogger().getIdx())
                    .build();

            blogDtoList.add(entityToDto(blogEntity, memberEntity));
        }
        return blogDtoList;
    }

    @Override
    public int update(BlogDto dto) {
        return 0;
    }

    @Override
    public int delete(BlogDto dto) {
        blogRepository.delete(dtoToEntity(dto)); // delete from blog where idx = ***; 과 동일한 것
        return 0;
    }
}
