package com.song.foundation.facade.service;

import com.song.base.facade.service.BaseFacadeService;
import com.song.foundation.entity.Accessory;

public interface AccessoryFacadeService extends BaseFacadeService<Accessory, Long> {

	public Accessory savePhoto(Accessory acc);
}
