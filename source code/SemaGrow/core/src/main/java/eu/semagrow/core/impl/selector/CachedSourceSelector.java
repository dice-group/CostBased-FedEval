package eu.semagrow.core.impl.selector;

import eu.semagrow.art.Loggable;
import eu.semagrow.core.source.SourceMetadata;
import eu.semagrow.core.source.SourceSelector;
import org.openrdf.query.BindingSet;
import org.openrdf.query.Dataset;
import org.openrdf.query.algebra.StatementPattern;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.helpers.StatementPatternCollector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Wraps a underlying @{link SourceSelector} and provides a simple
 * caching mechanism to tha source selection requests
 * @author Angelos Charalambidis
 */
public class CachedSourceSelector extends SourceSelectorWrapper
{
    final private org.slf4j.Logger logger =
    		org.slf4j.LoggerFactory.getLogger( CachedSourceSelector.class );

    private Map<StatementPattern, List<SourceMetadata>> cache = new HashMap<>();

    public CachedSourceSelector(SourceSelector selector) {
        super(selector);
    }

    @Override
    @Loggable
    public List<SourceMetadata> getSources(StatementPattern pattern, Dataset dataset, BindingSet bindings)
    {
        List<SourceMetadata> retv;

        if( cache.containsKey(pattern) ) {
            retv = cache.get(pattern);
        }
        else {
            List<SourceMetadata> list = super.getSources(pattern, dataset, bindings);
            cache.put(pattern, list);
            retv = list;
        }

        return retv;
    }

    @Override
    public List<SourceMetadata> getSources(Iterable<StatementPattern> patterns, Dataset dataset, BindingSet bindings)
    {
        List<SourceMetadata> list = new LinkedList<SourceMetadata>();
        for (StatementPattern p : patterns) {
            list.addAll(this.getSources(p, dataset, bindings));
        }
        return list;
    }

    @Override
    public List<SourceMetadata> getSources(TupleExpr expr, Dataset dataset, BindingSet bindings) {
        //FIXME: This is not the case in general but only in a pattern-wise src selector
        List<StatementPattern> patterns  = StatementPatternCollector.process( expr );
        return getSources( patterns, dataset, bindings );
    }


}
