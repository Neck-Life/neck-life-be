package com.necklife.api.service.inquiry;

import com.necklife.api.entity.member.InquiryEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.InquiryRepository;
import com.necklife.api.repository.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostInquiryService {

	private final InquiryRepository inquiryRepository;
	private final MemberRepository memberRepository;

	//    private final MailClient mailClient;

	public void execute(String memberId, String title, String content) {
		Optional<MemberEntity> findMember = memberRepository.findById(memberId);
		if (findMember.isEmpty()) {
			throw new IllegalArgumentException("Member not found");
		}
		//        mailClient.sendEmail("ccnoi1532@naver.com",title,content);

		InquiryEntity inquiry =
				InquiryEntity.builder().member(findMember.get()).title(title).content(content).build();
		inquiryRepository.save(inquiry);
	}
}
