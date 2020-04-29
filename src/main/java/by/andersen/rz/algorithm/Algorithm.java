package by.andersen.rz.algorithm;

import java.util.*;

public class Algorithm {

    /**
     * Бинарный поиск
     *
     * @param list
     * @param item
     * @return
     */
    private Integer binarySearch(List<Integer> list, int item) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int guess = list.get(mid);
            if (guess == item) {
                return mid;
            }
            if (guess > item) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return null;
    }

    /**
     * Сортировка выбором
     *
     * @param list
     * @return
     */
    private List<Integer> selectionSort(List<Integer> list) {
        List<Integer> resultList = new ArrayList<Integer>();
        List<Integer> temp = new ArrayList<Integer>(list);
        for (int i = 0; i < list.size(); i++) {
            int smallestIndex = findSmallest(temp);
            resultList.add(list.get(smallestIndex));
            temp.remove(smallestIndex);
        }
        return resultList;
    }

    private int findSmallest(List<Integer> list) {
        int smallestIndex = 0;
        int smallestItem = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) < smallestItem) {
                smallestItem = list.get(i);
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    /**
     * Рекурсия для суммы элементов массива.
     *
     * @param list
     * @return
     */
    private int sum(List<Integer> list) {
        int sum = 0;
        int temp = list.get(0);
        list.remove(0);
        if (list.size() == 0) {
            return sum;
        } else {
            sum = temp + sum(list);
        }
        return sum;
    }

    private List<Integer> quickSort(List<Integer> list) {
        if (list.size() < 2) {
            return list;
        } else {
            int pivot = list.get(0);
            List<Integer> less = new ArrayList<Integer>();
            List<Integer> grater = new ArrayList<Integer>();

            for (int i = 1; i < list.size(); i++) {
                int item = list.get(i);
                if (item > pivot) {
                    grater.add(item);
                } else {
                    less.add(item);
                }
            }
            List<Integer> temp = quickSort(less);
            List<Integer> result = new ArrayList<Integer>(temp);
            result.add(pivot);
            temp = quickSort(grater);
            result.addAll(temp);

            return result;
        }
    }

    private boolean search(String name) {
        Queue<String> queue = new LinkedList<String>();
        Map<String, String> checkedUsers = new HashMap<String, String>();

        Map<String, List<String>> map = initHashMap();

        addPersons(queue, map.get(name));

        while (!queue.isEmpty()) {
            String person = queue.poll();
            if (checkedUsers.get(person) == null) {
                if (person.matches("bob")) {
                    System.out.println(person + " is a mango seller");
                    return true;
                }
            } else {
                addPersons(queue, map.get(person));
                checkedUsers.put(person, "checked");
            }
        }
        return false;
    }

    private void addPersons(Queue<String> queue, List<String> temp) {
        for (String item : temp) {
            queue.offer(item);
        }
    }

    private Map<String, List<String>> initHashMap() {
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        List<String> you = new ArrayList<String>(Arrays.asList("alice", "bob", "claire"));
        map.put("you", you);

        List<String> bob = new ArrayList<String>(Arrays.asList("anuj", "peggy"));
        map.put("bob", bob);

        List<String> alice = new ArrayList<String>(Collections.singletonList("peggy"));
        map.put("alice", alice);

        List<String> claire = new ArrayList<String>(Arrays.asList("thom", "jonny"));
        map.put("claire", claire);

        List<String> anuj = new ArrayList<String>();
        map.put("anuj", anuj);

        List<String> peggy = new ArrayList<String>();
        map.put("peggy", peggy);

        List<String> thom = new ArrayList<String>();
        map.put("thom", thom);

        List<String> jonny = new ArrayList<String>();
        map.put("jonny", jonny);

        return map;
    }

    private void deikstraAlgorithm() {
        HashMap<String, HashMap<String, Integer>> graph = new HashMap<String, HashMap<String, Integer>>();
        HashMap<String, Integer> dots = new HashMap<String, Integer>();

        dots.put("a", 6);
        dots.put("b", 2);

        graph.put("start", dots);

        dots = new HashMap<String, Integer>();
        dots.put("fin", 1);

        graph.put("a", dots);

        dots = new HashMap<String, Integer>();
        dots.put("a", 3);
        dots.put("fin", 5);

        graph.put("b", dots);

        graph.put("fin", new HashMap<String, Integer>());

        Integer infinity = Integer.MAX_VALUE;
        HashMap<String, Integer> costs = new HashMap<String, Integer>();

        costs.put("a", 6);
        costs.put("b", 2);
        costs.put("fin", infinity);

        HashMap<String, String> parents = new HashMap<String, String>();
        parents.put("a", "start");
        parents.put("b", "start");
        parents.put("fin", null);

        HashMap<String, String> processed = new HashMap<String, String>();

        String node = findLowestCostNode(costs, processed);

        while (processed.size() != costs.size()) {
            int cost = costs.get(node);
            HashMap<String, Integer> neighbors = graph.get(node);
            for (Map.Entry<String, Integer> entry : neighbors.entrySet()) {
                String key = entry.getKey();
                int newCost = cost + neighbors.get(key);
                if (costs.get(key) > newCost) {
                    costs.put(key, newCost);
                    parents.put(key, node);
                }
            }
            processed.put(node, "checked");
            node = findLowestCostNode(costs, processed);
        }
        System.out.println(costs.get("fin"));
    }

    private String findLowestCostNode(HashMap<String, Integer> costs, HashMap<String, String> processed) {
        int lowerCost = Integer.MAX_VALUE;
        String lowestCostNode = null;
        for (Map.Entry<String, Integer> node : costs.entrySet()) {
            String key = node.getKey();
            int cost = costs.get(key);
            if (processed.get(key) == null && cost < lowerCost) {
                lowerCost = cost;
                lowestCostNode = key;
            }
        }
        return lowestCostNode;
    }

    private void radiostationDistribution() {
        Set<String> states = new HashSet<String>(Arrays.asList("mt", "wa", "or", "id", "nv", "ut", "ca", "az"));

        HashMap<String, HashSet<String>> stations = new HashMap<String, HashSet<String>>();
        stations.put("kone", new HashSet<String>(Arrays.asList("id", "nv", "ut")));
        stations.put("ktwo", new HashSet<String>(Arrays.asList("wa", "id", "mt")));
        stations.put("kthree", new HashSet<String>(Arrays.asList("or", "nv", "ca")));
        stations.put("kfour", new HashSet<String>(Arrays.asList("nv", "ut")));
        stations.put("kfive", new HashSet<String>(Arrays.asList("ca", "az")));

        Set<String> finalStations = new HashSet<String>();

        String bestStation = null;
        Set<String> statesCovered = new HashSet<String>();

        for (Map.Entry<String, HashSet<String>> station : stations.entrySet()) {
            String key = station.getKey();
            System.out.println("key= " + key);
            Set<String> statesForStation = station.getValue();
            Set<String> covered = intersection(states, statesForStation);
            if (covered.size() > statesCovered.size()) {
                bestStation = key;
                statesCovered = covered;
            }
            states.removeAll(statesCovered);
        }
        System.out.println(statesCovered.toString());

        finalStations.add(bestStation);
        System.out.println(finalStations.toString());
    }

    private Set<String> intersection(Set<String> first, Set<String> second) {
        System.out.println("first= " + first.toString());
        System.out.println("second= " + second.toString());
        Set<String> result = new HashSet<String>(second);
        System.out.println("hashcode " + result.hashCode() + " " + first.hashCode());
        result.retainAll(first);
        System.out.println("result= " + result.toString());
        return result;
    }
}
