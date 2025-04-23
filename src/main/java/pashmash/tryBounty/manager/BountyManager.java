package pashmash.tryBounty.manager;

import lombok.Getter;
import pashmash.tryBounty.TryBounty;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class BountyManager {

    private final HashMap<UUID, Long> localStorage = new HashMap<>();

    public BountyManager() {
        loadAllBounties();
    }

    private void loadAllBounties() {
        try (ResultSet resultSet = TryBounty.getInstance().getSqlUtil().getResult("SELECT * FROM Bounty")) {
            while (resultSet != null && resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("UUID"));
                long amount = resultSet.getLong("amount");
                localStorage.put(uuid, amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadLocal(UUID uuid) {
        try (ResultSet resultSet = TryBounty.getInstance().getSqlUtil().getResult("SELECT * FROM Bounty WHERE UUID=?", uuid.toString())) {
            if (resultSet != null && resultSet.next()) {
                long amount = resultSet.getLong("amount");
                localStorage.put(uuid, amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(UUID uuid, long amount) {
        TryBounty.getInstance().getSqlUtil().update("UPDATE Bounty SET amount=? WHERE UUID=?", amount, uuid.toString());
        localStorage.put(uuid, amount);
    }

    public void delete(UUID uuid) {
        TryBounty.getInstance().getSqlUtil().update("DELETE FROM Bounty WHERE UUID=?", uuid.toString());
        localStorage.remove(uuid);
    }

    public long get(UUID uuid) {
        return localStorage.getOrDefault(uuid, 1L);
    }
}