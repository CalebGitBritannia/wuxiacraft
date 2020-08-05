package com.airesnor.wuxiacraft.world;

import com.airesnor.wuxiacraft.world.data.WorldSectData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Sect {

    private String sectName;
    private UUID sectLeader;
    private List<Pair<String, Integer>> ranks;
    private List<Pair<String, Integer>> defaultRanks;
    private List<Pair<UUID, String>> members;
    private List<UUID> invitations;
    private List<String> allies;
    private List<String> enemies;

    private boolean disband;
    private long disbandTime;
    private boolean changeLeader;
    private long changeLeaderTime;


    public Sect(String sectName, UUID sectLeaderUUID) {
        this.sectName = sectName;
        this.sectLeader = sectLeaderUUID;
        this.ranks = new ArrayList<>();
        this.defaultRanks = new ArrayList<>();
        this.members = new ArrayList<>();
        this.invitations = new ArrayList<>();
        this.allies = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.disband = false;
        this.disbandTime = 0;
        this.changeLeader = false;
        this.changeLeaderTime = 0;

        this.ranks.add(Pair.of("ServiceDisciple", 0));
        this.ranks.add(Pair.of("OuterDisciple", 1));
        this.ranks.add(Pair.of("InnerDisciple", 2));
        this.ranks.add(Pair.of("CoreDisciple", 3));
        this.ranks.add(Pair.of("SectElder", 4));
        this.ranks.add(Pair.of("ViceSectLeader", 5));

        this.defaultRanks.add(Pair.of("ServiceDisciple", 0));
        this.defaultRanks.add(Pair.of("OuterDisciple", 1));
        this.defaultRanks.add(Pair.of("InnerDisciple", 2));
        this.defaultRanks.add(Pair.of("CoreDisciple", 3));
        this.defaultRanks.add(Pair.of("SectElder", 4));
        this.defaultRanks.add(Pair.of("ViceSectLeader", 5));
    }

    public Sect() {
        this.sectName = null;
        this.sectLeader = null;
        this.ranks = new ArrayList<>();
        this.defaultRanks = new ArrayList<>();
        this.members = new ArrayList<>();
        this.invitations = new ArrayList<>();
        this.allies = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.disband = false;
        this.disbandTime = 0;
        this.changeLeader = false;
        this.changeLeaderTime = 0;

        this.ranks.add(Pair.of("ServiceDisciple", 0));
        this.ranks.add(Pair.of("OuterDisciple", 1));
        this.ranks.add(Pair.of("InnerDisciple", 2));
        this.ranks.add(Pair.of("CoreDisciple", 3));
        this.ranks.add(Pair.of("SectElder", 4));
        this.ranks.add(Pair.of("ViceSectLeader", 5));

        this.defaultRanks.add(Pair.of("ServiceDisciple", 0));
        this.defaultRanks.add(Pair.of("OuterDisciple", 1));
        this.defaultRanks.add(Pair.of("InnerDisciple", 2));
        this.defaultRanks.add(Pair.of("CoreDisciple", 3));
        this.defaultRanks.add(Pair.of("SectElder", 4));
        this.defaultRanks.add(Pair.of("ViceSectLeader", 5));
    }

    public void setSectName(String sectName) {
        this.sectName = sectName;
    }

    public String getSectName() {
        return sectName;
    }

    public void setSectLeader(UUID sectLeader) {
        this.sectLeader = sectLeader;
    }

    public UUID getSectLeader() {
        return sectLeader;
    }

    public void addRank(String rankName, int permissionLevel) {
        for (Pair<String, Integer> rank : ranks) {
            if (!rank.getLeft().equalsIgnoreCase(rankName)) {
                ranks.add(Pair.of(rankName, permissionLevel));
                break;
            }
        }
        Comparator<Pair<String, Integer>> rankComparable = Comparator.comparingInt(Pair::getRight);
        ranks.sort(rankComparable);
    }

    public void removeRank(String rankName) {
        for (int i = 0; i < ranks.size(); i++) {
            Pair<String, Integer> rank = ranks.get(i);
            if (rank.getLeft().equalsIgnoreCase(rankName)) {
                ranks.remove(i);
                break;
            }
        }
    }

    public List<Pair<String, Integer>> getRanks() {
        return ranks;
    }

    public Pair<String, Integer> getRank(String rankName) {
        Pair<String, Integer> sectRank = null;
        for (Pair<String, Integer> rank : ranks) {
            if (rank.getLeft().equalsIgnoreCase(rankName)) {
                sectRank = rank;
                break;
            }
        }
        return sectRank;
    }

    public List<Pair<String, Integer>> getDefaultRanks() {
        return defaultRanks;
    }

    public void setRankPermissionLevel(String rankName, int permissionLevel) {
        for (int i = 0; i < ranks.size(); i++) {
            Pair<String, Integer> rank = ranks.get(i);
            if (rank.getLeft().equalsIgnoreCase(rankName)) {
                ranks.remove(i);
                ranks.add(i, Pair.of(rankName, permissionLevel));
                break;
            }
        }
    }

    public void addMember(UUID memberUUID, String rankName) {
        for (Pair<UUID, String> member : members) {
            if (!member.getLeft().equals(memberUUID)) {
                members.add(Pair.of(memberUUID, rankName));
                removePlayerFromInvitations(memberUUID);
                break;
            }
        }
    }

    public void removeMember(UUID memberUUID) {
        for (int i = 0; i < members.size(); i++) {
            UUID member = members.get(i).getLeft();
            if (member.equals(memberUUID)) {
                members.remove(i);
                break;
            }
        }
    }

    public List<Pair<UUID, String>> getMembers() {
        return members;
    }

    public Pair<UUID, String> getMemberByUUID(UUID uuid) {
        Pair<UUID, String> sectMember = null;
        for (Pair<UUID, String> member : members) {
            if (member.getLeft().equals(uuid)) {
                sectMember = member;
                break;
            }
        }
        return sectMember;
    }

    public void removePlayerFromInvitations(UUID playerUUID) {
        for (int i = 0; i < invitations.size(); i++) {
            if (invitations.get(i).equals(playerUUID)) {
                invitations.remove(i);
                break;
            }
        }
    }

    public void addPlayerToInvitation(UUID playerUUID) {
        for (UUID invitation : invitations) {
            if (!invitation.equals(playerUUID)) {
                invitations.add(playerUUID);
                break;
            }
        }
    }

    public List<UUID> getInvitations() {
        return invitations;
    }

    public void addAlly(String sectName) {
        for (String allyName : allies) {
            if (!allyName.equalsIgnoreCase(sectName)) {
                allies.add(sectName);
                break;
            }
        }
    }

    public void removeAlly(String sectName) {
        for (int i = 0; i < allies.size(); i++) {
            if (allies.get(i).equalsIgnoreCase(sectName)) {
                allies.remove(i);
                break;
            }
        }
    }

    public List<String> getAllies() {
        return allies;
    }

    public void addEnemy(String sectName) {
        for (String enemyName : enemies) {
            if (!enemyName.equalsIgnoreCase(sectName)) {
                enemies.add(sectName);
                break;
            }
        }
    }

    public void removeEnemy(String sectName) {
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).equalsIgnoreCase(sectName)) {
                enemies.remove(i);
                break;
            }
        }
    }

    public List<String> getEnemies() {
        return enemies;
    }

    public void setDisband(boolean disband) {
        this.disband = disband;
    }

    public boolean isDisbanding() {
        return disband;
    }

    public void setDisbandTime(long disbandTime) {
        this.disbandTime = disbandTime;
    }

    public long getDisbandTime() {
        return disbandTime;
    }

    public void setChangeLeader(boolean changeLeader) {
        this.changeLeader = changeLeader;
    }

    public boolean isChangingLeader() {
        return changeLeader;
    }

    public void setChangeLeaderTime(long changeLeaderTime) {
        this.changeLeaderTime = changeLeaderTime;
    }

    public long getChangeLeaderTime() {
        return changeLeaderTime;
    }

    public Sect readFromNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        this.setSectName(nbt.getString("sectName"));
        this.setSectLeader(nbt.getUniqueId("sectLeader"));
        NBTTagList rankList = (NBTTagList) nbt.getTag("rankList");
        NBTTagList memberList = (NBTTagList) nbt.getTag("memberList");
        NBTTagList invitationList = (NBTTagList) nbt.getTag("invitationList");
        NBTTagList allyList = (NBTTagList) nbt.getTag("allyList");
        NBTTagList enemyList = (NBTTagList) nbt.getTag("enemyList");
        for (int i = 0; i < rankList.tagCount(); i++) {
            NBTTagCompound tagCompound = rankList.getCompoundTagAt(i);
            addRank(tagCompound.getString("rankName"), tagCompound.getInteger("rankPermissionLevel"));
        }
        for (int i = 0; i < memberList.tagCount(); i++) {
            NBTTagCompound tagCompound = memberList.getCompoundTagAt(i);
            addMember(tagCompound.getUniqueId("memberUUID"), tagCompound.getString("memberRank"));
        }
        for (int i = 0; i < invitationList.tagCount(); i++) {
            NBTTagCompound tagCompound = invitationList.getCompoundTagAt(i);
            addPlayerToInvitation(tagCompound.getUniqueId("playerInvitationUUID"));
        }
        for (int i = 0; i < allyList.tagCount(); i++) {
            NBTTagCompound tagCompound = allyList.getCompoundTagAt(i);
            addAlly(tagCompound.getString("ally"));
        }
        for (int i = 0; i < enemyList.tagCount(); i++) {
            NBTTagCompound tagCompound = enemyList.getCompoundTagAt(i);
            addEnemy(tagCompound.getString("enemy"));
        }
        this.setDisband(nbt.getBoolean("disbanding"));
        this.setDisbandTime(nbt.getLong("disbandTime"));
        this.setChangeLeader(nbt.getBoolean("changeLeader"));
        this.setChangeLeaderTime(nbt.getLong("changeLeaderTime"));
        return this;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList rankList = new NBTTagList();
        NBTTagList memberList = new NBTTagList();
        NBTTagList invitationList = new NBTTagList();
        NBTTagList allyList = new NBTTagList();
        NBTTagList enemyList = new NBTTagList();
        tag.setString("sectName", sectName);
        tag.setUniqueId("sectLeader", sectLeader);
        for (Pair<String, Integer> rank : ranks) {
            NBTTagCompound rankCompound = new NBTTagCompound();
            rankCompound.setString("rankName", rank.getLeft());
            rankCompound.setInteger("rankPermissionLevel", rank.getRight());
            rankList.appendTag(rankCompound);
        }
        for (Pair<UUID, String> member : members) {
            NBTTagCompound memberCompound = new NBTTagCompound();
            memberCompound.setUniqueId("memberUUID", member.getLeft());
            memberCompound.setString("memberRank", member.getRight());
            memberList.appendTag(memberCompound);
        }
        for (UUID invitation : invitations) {
            NBTTagCompound invitationCompund = new NBTTagCompound();
            invitationCompund.setUniqueId("playerInvitationUUID", invitation);
            invitationList.appendTag(invitationCompund);
        }
        for (String ally : allies) {
            NBTTagCompound allyCompound = new NBTTagCompound();
            allyCompound.setString("ally", ally);
            allyList.appendTag(allyCompound);
        }
        for (String enemy : enemies) {
            NBTTagCompound enemyCompound = new NBTTagCompound();
            enemyCompound.setString("enemy", enemy);
            enemyList.appendTag(enemyCompound);
        }
        tag.setTag("rankList", rankList);
        tag.setTag("memberList", memberList);
        tag.setTag("invitationList", invitationList);
        tag.setTag("allyList", allyList);
        tag.setTag("enemyList", enemyList);
        tag.setBoolean("disbanding", disband);
        tag.setLong("disbandTime", disbandTime);
        tag.setBoolean("changeLeader", changeLeader);
        tag.setLong("changeLeaderTime", changeLeaderTime);
        return tag;
    }

    public String getDefaultRank() {
        return ranks.get(0).getLeft();
    }

    public static Sect getSectByPlayer(EntityPlayerMP playerMP, WorldSectData sectData) {
        boolean isThis = false;
        Sect chosenSect = null;
        for (Sect sect : sectData.SECTS) {
            List<Pair<UUID, String>> members = sect.getMembers();
            UUID sectLeader = sect.getSectLeader();
            for (Pair<UUID, String> member : members) {
                if (member.getLeft().equals(playerMP.getUniqueID())) {
                    isThis = true;
                    break;
                }
            }
            if (!isThis) {
                if (sectLeader.equals(playerMP.getUniqueID())) {
                    isThis = true;
                }
            }
            if (isThis) {
                chosenSect = sect;
                break;
            }
        }
        return chosenSect;
    }

    public static Sect getSectByName(String sectName, WorldSectData sectData) {
        Sect chosenSect = null;
        for (Sect sect : sectData.SECTS) {
            if (sect.sectName.equalsIgnoreCase(sectName)) {
                chosenSect = sect;
                break;
            }
        }
        return chosenSect;
    }
}
