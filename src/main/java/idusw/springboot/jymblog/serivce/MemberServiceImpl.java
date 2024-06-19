package idusw.springboot.jymblog.serivce;

import idusw.springboot.jymblog.entity.MemberEntity;
import idusw.springboot.jymblog.model.MemberDto;
import idusw.springboot.jymblog.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    final MemberRepository memberRepository;
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public int create(MemberDto memberDto) {
        int ret = 0;
        MemberEntity entity = dtoToEntity(memberDto);
        memberRepository.save(entity);
        ret = 1;
        return ret;
    }

    @Override
    public MemberDto readByIdx(Long idx) {
        if(!memberRepository.findByIdx(idx).isEmpty())
            return entityToDto(memberRepository.findByIdx(idx).get());
        else
            return null;
    }

    @Override
    public List<MemberDto> readAll() {
        List<MemberEntity> list = memberRepository.findAll();
        List<MemberDto> dtoList = new ArrayList<MemberDto>();
        for (MemberEntity memberEntity : list) {
            dtoList.add(entityToDto(memberEntity));
        }
        return dtoList;
    }

    @Override
    public int update(MemberDto memberDto) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findById(memberDto.getIdx());
        if (memberEntityOptional.isPresent()) {
            MemberEntity existingEntity = memberEntityOptional.get();
            // 기존 엔티티에서 새로운 정보를 업데이트합니다.
            existingEntity.setName(memberDto.getName());
            existingEntity.setEmail(memberDto.getEmail());

            // 엔티티를 저장하여 업데이트를 반영합니다.
            memberRepository.save(existingEntity);
            return 1; // 업데이트 성공
        } else {
            return 0; // 업데이트 실패
        }
    }


    @Override
    public int delete(MemberDto memberDto) {
        Long idx = memberDto.getIdx();
        if (idx == null) {
            System.out.println("delete: idx is null");
            return 0;
        }

        Optional<MemberEntity> memberEntityOptional = memberRepository.findById(idx);
        if (memberEntityOptional.isPresent()) {
            memberRepository.delete(memberEntityOptional.get());
            System.out.println("delete: member deleted successfully");
            return 1;
        } else {
            System.out.println("delete: member not found");
            return 0;
        }
    }

    @Override
    public MemberDto loginById(MemberDto memberDto) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByIdAndPw(memberDto.getId(), memberDto.getPw());
        return memberEntityOptional.map(this::entityToDto).orElse(null);
    }
}
