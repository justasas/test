FactoryGirl.define do
  factory :category do |c|
    c.sequence(:name) { |n| "category_name#{n}" }
    c.description 'MyText012345'
  end
end
