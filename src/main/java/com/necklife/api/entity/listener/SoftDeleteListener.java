package com.necklife.api.entity.listener;

import com.necklife.api.entity.BaseEntity;
import jakarta.persistence.PreRemove;

/** 엔티티에 삭제되었다는 정보를 반영하기 위해서 리스너가 필요하다. */
public class SoftDeleteListener {

	@PreRemove
	private void preRemove(BaseEntity entity) {
		entity.delete();
	}
}
