#!/usr/bin/env bash

# 1. 기존 기본 라벨 삭제 (선택 사항: 깔끔한 시작을 위해)
#labels=(bug documentation duplicate enhancement "good first issue" "help wanted" invalid question wontfix)
#
#for label in "${labels[@]}"; do
#  gh label delete "$label" --yes 2>/dev/null
#done

# 라벨이 이미 있으면 수정, 없으면 생성 (idempotent)
upsert_label() {
  local name="$1"
  local color="$2"
  local description="$3"

  gh label create "$name" --color "$color" --description "$description" >/dev/null 2>&1 \
    || gh label edit "$name" --color "$color" --description "$description"
}

# 2. 백엔드 추천 라벨 생성
# 형식: gh label create "이름" --color "색상코드" --description "설명"

upsert_label "✨ feat" "a2eeef" "새로운 백엔드 기능 추가 (New backend feature)"
upsert_label "🐛 bug" "d73a4a" "서버/로직 버그 수정 (Backend bug fix)"
upsert_label "♻️ refactor" "e99695" "백엔드 코드 구조 개선 (Code restructuring)"
upsert_label "🚚 chore" "ededed" "의존성/설정/인프라 유지보수 (Maintenance)"
upsert_label "🧩 api" "1d76db" "API 엔드포인트 및 스펙 변경 (API changes)"
upsert_label "🗄️ db" "5319e7" "DB 스키마/쿼리/마이그레이션 변경 (Database changes)"
upsert_label "⚡ performance" "fbca04" "성능 개선 및 병목 최적화 (Performance optimization)"
upsert_label "✅ test" "7057ff" "백엔드 테스트 코드 추가/수정 (Backend tests)"
upsert_label "📝 docs" "0075ca" "백엔드 문서 수정 (Backend documentation)"
upsert_label "🔒 security" "b60205" "보안 취약점 대응 및 강화 (Security hardening)"

# 3. 이슈 폼의 처리 시기 라벨
upsert_label "P0" "b60205" "즉시 처리해야 하는 최우선 작업"
upsert_label "P1" "d93f0b" "가까운 시일 내 처리해야 하는 작업"
upsert_label "P2" "fbca04" "일정에 맞춰 처리할 일반 작업"
upsert_label "P3" "0e8a16" "여유가 있을 때 처리할 작업"

# 4. 이슈 폼의 중요도 라벨
upsert_label "Critical" "b60205" "서비스 운영에 치명적인 영향을 주는 이슈"
upsert_label "High" "d93f0b" "사용자 또는 주요 기능에 큰 영향을 주는 이슈"
upsert_label "Medium" "fbca04" "일부 기능에 영향을 주는 일반 이슈"
upsert_label "Low" "0e8a16" "영향이 작거나 개선 성격의 이슈"

echo "✅ 백엔드 라벨이 성공적으로 생성되었습니다!"
