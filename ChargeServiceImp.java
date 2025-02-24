package kr.kh.onAirAuction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.onAirAuction.dao.ChargeDAO;
import kr.kh.onAirAuction.vo.ChargeVO;

@Service
public class ChargeServiceImp implements ChargeService {

	@Autowired
	ChargeDAO chargeDao;

	@Override
	public void insertCharge(ChargeVO chargeVO) {
		chargeDao.chargeInsert(chargeVO);			
	}


}
