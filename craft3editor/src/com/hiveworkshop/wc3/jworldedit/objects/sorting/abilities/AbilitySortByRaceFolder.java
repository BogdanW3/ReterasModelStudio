package com.hiveworkshop.wc3.jworldedit.objects.sorting.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import com.hiveworkshop.wc3.jworldedit.objects.sorting.AbstractSortingFolderTreeNode;
import com.hiveworkshop.wc3.jworldedit.objects.sorting.SortingFolderTreeNode;
import com.hiveworkshop.wc3.jworldedit.objects.sorting.general.SortRace;
import com.hiveworkshop.wc3.units.objectdata.MutableObjectData.MutableGameObject;
import com.hiveworkshop.wc3.units.objectdata.War3ID;

public final class AbilitySortByRaceFolder extends AbstractSortingFolderTreeNode {

	/**
	 * default generated id to stop warnings, not going to serialize these folders
	 */
	private static final long serialVersionUID = 1L;
	private static final War3ID ABIL_RACE_FIELD = War3ID.fromString("arac");

	private final Map<String, SortingFolderTreeNode> raceFolders;
	private final List<SortingFolderTreeNode> raceNodes;

	public AbilitySortByRaceFolder(final String displayName) {
		this(displayName, Arrays.asList(DefaultAbilityRace.values()));
	}

	public AbilitySortByRaceFolder(final String displayName, final List<SortRace> races) {
		super(displayName);
		raceFolders = new HashMap<>();
		raceNodes = new ArrayList<>();
		for (final SortRace race : races) {
			final AbilityRaceLevelFolder meleeCampaignFolder = new AbilityRaceLevelFolder(race.getDisplayName());
			raceFolders.put(race.getKeyString(), meleeCampaignFolder);
			raceNodes.add(meleeCampaignFolder);
		}
	}

	private DefaultAbilityRace raceKey(final int index) {
		switch (index) {
		case -1:
			return DefaultAbilityRace.HUMAN;
		case 0:
			return DefaultAbilityRace.HUMAN;
		case 1:
			return DefaultAbilityRace.ORC;
		case 2:
			return DefaultAbilityRace.UNDEAD;
		case 3:
			return DefaultAbilityRace.NIGHTELF;
		case 4:
			return DefaultAbilityRace.OTHER;
		case 5:
			return DefaultAbilityRace.NEUTRAL_HOSTILE;
		case 6:
			return DefaultAbilityRace.NEUTRAL_PASSIVE;
		}
		return DefaultAbilityRace.NEUTRAL_PASSIVE;
	}

	@Override
	public SortingFolderTreeNode getNextNode(final MutableGameObject object) {
		String race = object.getFieldAsString(ABIL_RACE_FIELD, 0);
		DefaultAbilityRace raceKey = null;
		if ("naga".equals(race)) {
			race = "demon";
		}
		for (int i = 0; i < 6; i++) {
			if (race.equals(raceKey(i).getKeyString())) {
				raceKey = raceKey(i);
			}
		}
		if (raceKey == null) {
			if (raceFolders.containsKey(race)) {
				return raceFolders.get(race);
			} else {
				raceKey = DefaultAbilityRace.OTHER;
			}
		}
		final SortingFolderTreeNode sortingFolderTreeNode = raceFolders.get(raceKey.getKeyString());
		return sortingFolderTreeNode;
	}

	@Override
	public int getSortIndex(final DefaultMutableTreeNode childNode) {
		return raceNodes.indexOf(childNode);
	}
}
