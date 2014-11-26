package br.com.metricminer2.metric;

import java.util.Map;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.domain.Modification;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.SCMRepository;

public class MethodLevelMetricCalculator implements MetricCalculator {

	private MethodLevelCodeMetric metric;

	public MethodLevelMetricCalculator(MethodLevelCodeMetric metric) {
		this.metric = metric;
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		
		for(Modification change : commit.getModifications()) {
			if(metric.accepts(change.getNewPath())) {
				Map<String, Double> values = metric.calculate(change.getSourceCode());
				for(Map.Entry<String, Double> entry : values.entrySet()) {
					String method = entry.getKey();
					Double value = entry.getValue();
					writer.write(commit.getHash(), change.getNewPath(), method, value);
				}
			}
		}

	}

	@Override
	public String name() {
		return "Method Level metric Processor for " + metric.getName();
	}

}