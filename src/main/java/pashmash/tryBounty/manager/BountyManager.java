package pashmash.tryBounty.manager;

import lombok.Getter;
import pashmash.tryBounty.TryBounty;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class BountyManager {

    private final HashMap<UUID, Integer> localStorage = new HashMap<>();

    public BountyManager() {
        ResultSet resultSet = TryBounty.getInstance().getSqlUtil().getResult("SELECT * FROM Bounty");
        try {
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("UUID"));
                int amount = resultSet.getInt("amount");
                localStorage.put(uuid, amount);
            }
        } catch (Exception ignored) { }
    }

    public void loadLocal(UUID uuid) {
        ResultSet resultSet = TryBounty.getInstance().getSqlUtil().getResult("SELECT * FROM Bounty WHERE UUID='" + uuid.toString() + "'");
        try {
            if (resultSet.next()) {
                int amount = resultSet.getInt("amount");
                localStorage.put(uuid, amount);
            }
        } catch (Exception ignored) { }
    }

    public void set(UUID uuid, int amount) {
        TryBounty.getInstance().getSqlUtil().update("UPDATE Bounty SET amount=" + amount + " WHERE UUID='" + uuid.toString() + "'");
        localStorage.put(uuid, amount);
    }

    public void delete(UUID uuid) {
        TryBounty.getInstance().getSqlUtil().update("DELETE FROM Bounty WHERE UUID='" + uuid.toString() + "'");
        localStorage.remove(uuid);
    }

    public int get(UUID uuid) {
        if (localStorage.containsKey(uuid)) {
            return localStorage.get(uuid);
        } else {
            String amountString = TryBounty.getInstance().getSqlUtil().get("Bounty", "amount", "UUID='" + uuid.toString() + "'");
            int amount = amountString != null ? Integer.parseInt(amountString) : 0;
            localStorage.put(uuid, amount);
            return amount;
        }
    }
}