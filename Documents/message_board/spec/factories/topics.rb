FactoryGirl.define do
  factory :topic do
    # account
    title 'title111111'
    # replies_count 1
    factory :topic_with_reply do
      transient do
        replies_count 1
      end
      before :create do |topic, evaluator|
        topic.replies <<
          FactoryGirl.create_list(:reply, evaluator.replies_count,
                                  topic: topic)
      end
    end
  end
end
