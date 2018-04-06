package xyz.inux.pluto.common.hash;

import java.util.*;

public class ConsistentHash<T> {
	private static final int default_number_replicas = 100;
	
	private final HashFunction hashFunction; 
	private final int numberOfReplicas; 
	private final SortedMap<Long, T> circle = new TreeMap<Long, T>();
	
	public ConsistentHash(Collection<T> nodes) {
		this(default_number_replicas, nodes);
	}
	
	public ConsistentHash(int numberOfReplicas, Collection<T> nodes) {
		this(new MurMurHashFunction(), numberOfReplicas, nodes);
	}
	
	public ConsistentHash(HashFunction hashFunction, Collection<T> nodes) {
		this(hashFunction, default_number_replicas, nodes);
	}
	
	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
		this.hashFunction = hashFunction;
		if(numberOfReplicas <= 0){
			this.numberOfReplicas = 1;
		}else{
			this.numberOfReplicas = numberOfReplicas;
		}
		
		for (T node : nodes) {
			add(node);
		}
	}
	
	public void add(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.toString() + i), node);
		}
	}
	
	public void remove(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + i));
		}
	}
	
	public T get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		long hash = hashFunction.hash(key);
		
		if (!circle.containsKey(hash)) {
			SortedMap<Long, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		
		return circle.get(hash);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] myNodes = {"A", "B"};
		List<String> list = Arrays.asList(myNodes);
		ConsistentHash<String> consistentHash = new ConsistentHash<String>(200, list);
		Map<String, Integer> statisticMap = new HashMap<String, Integer>();
		
		for(int i = 0; i < 10; i ++){
			//long lastValue = (long)(Math.random()*System.currentTimeMillis()*(i+1));
			String key = "" + i;
			String value = consistentHash.get(key);
			System.out.println(key + ":" + value);
			int count = 0;
			if(statisticMap.containsKey(value)){
				count = statisticMap.get(value);
			}else{
				count = 0;
			}
			statisticMap.put(value, count+1);
		}
		for(String node : myNodes){
			System.out.println(node + ":" + statisticMap.get(node));
		}
	}

}
