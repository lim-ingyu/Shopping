package kr.kh.onAirAuction.dao;

import org.apache.ibatis.annotations.Param;

import kr.kh.onAirAuction.vo.ChargeVO;

public interface ChargeDAO {

	void chargeInsert(ChargeVO chargeVO);

}
