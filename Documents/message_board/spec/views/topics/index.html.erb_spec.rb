require 'rails_helper'

RSpec.describe 'topics/index.html.erb', type: :view do
  it 'should display topics correctly' do
    assign(:topics, [FactoryGirl.create(:topic_with_reply,
                                        title: 'verynicetitle'),
                     FactoryGirl.create(:topic_with_reply,
                                        title: 'verynicetitle2')])

    render

    expect(rendered).to match(/Title/)
    expect(rendered).to match(/Author/)
    expect(rendered).to match(/verynicetitle/)
    expect(rendered).to match(/verynicetitle2/)
    expect(rendered).to match(/Show/)
    expect(rendered).to match(/Edit/)
  end

  it 'should display Log in and not display Log out if
  session[:user_id] is nil' do
    assign(:topics, [FactoryGirl.create(:topic_with_reply,
                                        title: 'verynicetitle')])
    session[:user_id] = nil

    render

    expect(rendered).to match(/Log in/)
    expect(rendered).not_to match(/Log out/)
  end

  it 'should display Log out and not display Log in if
  session[:user_id] is not nil' do
    assign(:topics, [FactoryGirl.create(:topic_with_reply,
                                        title: 'verynicetitle')])
    session[:user_id] = 4

    render

    expect(rendered).not_to match(/Log in/)
    expect(rendered).to match(/Log out/)
  end
end
