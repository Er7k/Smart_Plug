    package Server;

    import java.util.HashMap;
    import java.util.Map;

    public class ApplianceConsumptionModel {
        private final Map<String, Double> consumptionMap = new HashMap<>();

        // Lägg till eller uppdatera förbrukning för en apparat
        public synchronized void updateConsumption(String applianceName, double consumption) {
            consumptionMap.put(applianceName, consumption);
        }

        // Hämta förbrukning för en specifik apparat
        public synchronized double getConsumption(String applianceName) {
            return consumptionMap.getOrDefault(applianceName, 0.0);
        }

        // Beräkna total förbrukning för alla apparater
        public synchronized double getTotalConsumption() {
            return consumptionMap.values().stream().mapToDouble(Double::doubleValue).sum();
        }
    }
