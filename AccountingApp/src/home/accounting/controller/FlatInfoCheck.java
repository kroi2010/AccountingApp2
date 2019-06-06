package home.accounting.controller;

import java.util.List;

import home.accounting.DA.FlatBankDA;
import home.accounting.model.Flat;

public class FlatInfoCheck {

	public static Boolean start(Flat flat) {
		boolean allInfo = true;
		if (flat.getOwnerName() == null || flat.getOwnerName().isEmpty()) {
			allInfo = false;
		} else if (flat.getOwnerLastName() == null || flat.getOwnerLastName().isEmpty()) {
			allInfo = false;
		} else if (flat.getAreaSize() == null || flat.getAreaSize() == 0) {
			allInfo = false;
		} else if (flat.getPeopleLiving() == null || flat.getPeopleLiving() == 0) {
			allInfo = false;
		} else if(FlatBankDA.getAll(flat)==null || FlatBankDA.getAll(flat).isEmpty()){
			allInfo = false;
		}
		return allInfo;
	}

	public static Boolean all(List<Flat> flats) {
		boolean allGood = true;
		int iterator = 0;
		while (allGood && iterator < flats.size()) {
			allGood = FlatInfoCheck.start(flats.get(iterator));
			iterator++;
		}
		return allGood;
	}
}
