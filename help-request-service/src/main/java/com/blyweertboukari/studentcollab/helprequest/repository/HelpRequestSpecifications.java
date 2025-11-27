package com.blyweertboukari.studentcollab.helprequest.repository;

import com.blyweertboukari.studentcollab.helprequest.model.HelpRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

import java.time.Instant;
import java.util.List;

public class HelpRequestSpecifications {
    public static Specification<HelpRequest> hasStatuses(List<HelpRequest.Status> statuses) {
        return (root, query, cb) -> {
            if (statuses == null || statuses.isEmpty()) return null;
            return root.get("status").in(statuses);
        };
    }

    public static Specification<HelpRequest> hasAuthor(Long authorId) {
        return (root, query, cb) -> {
            if (authorId == null) return null;
            return cb.equal(root.get("authorId"), authorId);
        };
    }

    public static Specification<HelpRequest> hasAssignee(Long assigneeId) {
        return (root, query, cb) -> {
            if (assigneeId == null) return null;
            return cb.equal(root.get("assigneeId"), assigneeId);
        };
    }

    public static Specification<HelpRequest> desiredDateFrom(Instant from) {
        return (root, query, cb) -> {
            if (from == null) return null;
            return cb.greaterThanOrEqualTo(root.get("desiredDate"), from);
        };
    }

    public static Specification<HelpRequest> desiredDateTo(Instant to) {
        return (root, query, cb) -> {
            if (to == null) return null;
            return cb.lessThanOrEqualTo(root.get("desiredDate"), to);
        };
    }

    public static Specification<HelpRequest> hasAllKeywords(List<String> keywords) {
        return (root, query, cb) -> {
            if (keywords == null || keywords.isEmpty()) return null;

            Join<HelpRequest, String> keywordsJoin = root.join("keywords");

            var predicate = keywordsJoin.in(keywords);

            assert query != null;
            query.groupBy(root.get("id"));

            query.having(cb.equal(cb.countDistinct(keywordsJoin), keywords.size()));

            return predicate;
        };
    }
}
